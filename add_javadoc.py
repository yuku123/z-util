#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
为 z-util 项目所有模块的 public 类和 public/protected 方法补充 Javadoc
"""
import os
import re

MODULES = [
    'z-util-core', 'z-util-http', 'z-util-proxy', 'z-util-jdbc', 'z-util-workflow',
    'z-util-ml', 'z-util-monitor', 'z-util-math', 'z-util-media', 'z-util-devops',
    'z-util-crawler', 'z-util-office', 'z-util-source', 'z-util-visualization',
    'z-util-cache', 'z-util-cli', 'z-util-validation', 'z-util-distribute',
    'z-util-ch', 'z-util-dsl',
    'z-util-parser/z-util-parser-json', 'z-util-parser/z-util-parser-yaml',
    'z-util-parser/z-util-parser-properties', 'z-util-parser/z-util-parser-xml',
    'z-util-parser/z-util-parser-csv', 'z-util-parser/z-util-parser-ini',
    'z-util-parser/z-util-parser-toml', 'z-util-parser/z-util-parser-proto',
    'z-util-parser/z-util-parser-yaml', 'z-util-parser',
    'z-util-expr', 'z-util-zex',
]


def has_proper_javadoc(content, decl_start):
    """检查声明前是否有紧贴的、格式正确的 Javadoc（不被其他非空白内容隔开）"""
    if decl_start == 0:
        return False

    # 1. 从 decl_start 向前跳过空白，找到最近的 */
    i = decl_start
    while i > 0 and content[i - 1] in ' \t\n\r':
        i -= 1
    # i 现在指向 decl_start 之前第一个非空白字符的下一位
    if i < 2 or content[i - 2:i] != '*/':
        return False

    # 2. 从 i 继续向前找匹配的 /**
    #    i 指向 */ 之后的位置。i-2 指向 *，i-3 是 * 前的字符。
    #    向前找形如 "/**" 的三字符序列。
    j = i - 3
    while j >= 2:
        if content[j - 2:j + 1] == '/**':
            return True
        j -= 1
    return False


def find_public_classes(content):
    """查找所有 public 类/接口/枚举"""
    classes = []
    lines = content.split('\n')
    for i, line in enumerate(lines):
        line_stripped = line.strip()
        match = re.match(r'^public\s+(class|interface|enum|record|@interface)\s+(\w+)', line_stripped)
        if match:
            pos = sum(len(l) + 1 for l in lines[:i])
            classes.append({
                'name': match.group(2),
                'type': match.group(1),
                'line': i + 1,
                'pos': pos
            })
    return classes


def find_public_methods(content):
    """查找所有 public/protected 方法"""
    methods = []
    lines = content.split('\n')
    i = 0
    while i < len(lines):
        line = lines[i].strip()
        if re.match(r'^(public|protected)\s+', line):
            method_lines = [line]
            j = i + 1
            open_p = line.count('(')
            close_p = line.count(')')
            diff = open_p - close_p

            while j < len(lines) and diff > 0:
                method_lines.append(lines[j])
                open_p += lines[j].count('(')
                close_p += lines[j].count(')')
                diff = open_p - close_p
                j += 1

            if diff == 0:
                full = ' '.join(method_lines)
                paren = full.find('(')
                if paren > 0:
                    before = full[:paren].strip()
                    after = full[paren:]

                    mod_match = re.match(r'^(public|protected)', before)
                    if mod_match:
                        remaining = before[mod_match.end():].strip()
                        parts = remaining.rsplit(None, 1)
                        if len(parts) == 2:
                            ret_type, name = parts
                        elif len(parts) == 1:
                            ret_type, name = 'void', parts[0]
                        else:
                            i = j
                            continue

                        params_m = re.match(r'\(([^)]*)\)', after)
                        params = params_m.group(1) if params_m else ''
                        pos = sum(len(l) + 1 for l in lines[:i])

                        methods.append({
                            'name': name,
                            'return_type': ret_type,
                            'params': params,
                            'line': i + 1,
                            'pos': pos
                        })
                i = j - 1
        i += 1
    return methods


def simple_type(t):
    if not t or t.strip() == '':
        return 'Object'
    t = t.strip()
    if t == 'var':
        return 'var'
    mp = {
        'String': 'String', 'int': 'int', 'Integer': 'int', 'long': 'long', 'Long': 'long',
        'boolean': 'boolean', 'Boolean': 'boolean', 'double': 'double', 'Double': 'double',
        'float': 'float', 'Float': 'float', 'short': 'short', 'Short': 'short',
        'byte': 'byte', 'Byte': 'byte', 'char': 'char', 'Char': 'char',
        'void': 'void', 'Object': 'Object', 'List': 'List', 'Map': 'Map',
        'Set': 'Set', 'Collection': 'Collection', 'Iterable': 'Iterable',
        'Iterator': 'Iterator', 'Optional': 'Optional', 'Stream': 'Stream',
        'BigDecimal': 'BigDecimal', 'BigInteger': 'BigInteger', 'Date': 'Date', 'Calendar': 'Calendar',
    }
    if '<' in t:
        base = t.split('<')[0].strip()
        return f"{mp.get(base, base)}{t[len(base):]}"
    return mp.get(t, t)


def gen_class_javadoc(cls):
    d = {'class': f'{cls["name"]}类', 'interface': f'{cls["name"]}接口',
         'enum': f'{cls["name"]}枚举', '@interface': f'{cls["name"]}注解',
         'record': f'{cls["name"]}记录类'}
    desc = d.get(cls['type'], f'{cls["name"]}')
    return f'/**\n * {desc}。\n */'


def gen_method_javadoc(m):
    name = m['name']
    ret = simple_type(m['return_type'])
    params = m['params']

    pds = []
    if params.strip():
        pl = []
        depth = 0
        cur = ''
        for c in params:
            if c == '<':
                depth += 1
            elif c == '>':
                depth -= 1
            elif c == ',' and depth == 0:
                if cur.strip(): pl.append(cur.strip())
                cur = ''
            else:
                cur += c
        if cur.strip(): pl.append(cur.strip())

        for p in pl:
            p = p.strip()
            if not p: continue
            pts = p.split()
            if len(pts) >= 2:
                pt, pn = simple_type(pts[0]), pts[-1]
            elif len(pts) == 1:
                pt, pn = 'Object', pts[0]
            else:
                continue
            pds.append(f'@param {pn} {pt}类型参数')

    ret_j = '' if ret == 'void' else f'\n     * @return {ret}类型返回值'
    pj = '\n'.join(f'     * {pd}' for pd in pds) if pds else ''

    if pj:
        return f'    /**\n     * {name}方法。\n     * {pj}{ret_j}\n     */'
    else:
        return f'    /**\n     * {name}方法。{ret_j}\n     */'


def process_file(filepath):
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
    except:
        return 0, 0

    # 跳过注释文件
    stripped = content.strip()
    if stripped.startswith('//') and '\n' not in stripped[2:]:
        return 0, 0

    orig = content
    ca, ma = 0, 0

    try:
        # 处理类
        classes = find_public_classes(content)
        for cls in reversed(classes):
            if not has_proper_javadoc(content, cls['pos']):
                content = content[:cls['pos']] + gen_class_javadoc(cls) + '\n' + content[cls['pos']:]
                ca += 1

        # 处理方法
        methods = find_public_methods(content)
        for m in reversed(methods):
            if not has_proper_javadoc(content, m['pos']):
                content = content[:m['pos']] + gen_method_javadoc(m) + '\n' + content[m['pos']:]
                ma += 1
    except Exception as e:
        print(f"Error {filepath}: {e}")
        return 0, 0

    if content != orig:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)

    return ca, ma


def main():
    base = '/Users/zifang/workplace/idea_workplace/z-util'
    tc, tm = 0, 0

    for mod in MODULES:
        mp = os.path.join(base, mod)
        if not os.path.exists(mp): continue
        js = os.path.join(mp, 'src', 'main', 'java')
        if not os.path.exists(js): continue

        print(f"\n处理模块: {mod}")
        mc, mm, fc = 0, 0, 0

        for root, dirs, files in os.walk(js):
            for fn in files:
                if not fn.endswith('.java') or fn in ['package-info.java', 'module-info.java']: continue
                fpath = os.path.join(root, fn)
                c, m = process_file(fpath)
                if c or m:
                    fc += 1
                    mc += c
                    mm += m

        # 同时处理 test 目录
        ts = os.path.join(mp, 'src', 'test', 'java')
        if os.path.exists(ts):
            print(f"  处理 test 目录: {ts}")
            for root, dirs, files in os.walk(ts):
                for fn in files:
                    if not fn.endswith('.java') or fn in ['package-info.java', 'module-info.java']: continue
                    fpath = os.path.join(root, fn)
                    c, m = process_file(fpath)
                    if c or m:
                        fc += 1
                        mc += c
                        mm += m

        print(f"  文件: {fc}, 类Javadoc: {mc}, 方法Javadoc: {mm}")
        tc += mc
        tm += mm

    print(f"\n{'=' * 50}")
    print(f"总计增加: {tc} 个类Javadoc, {tm} 个方法Javadoc")


if __name__ == '__main__':
    main()
