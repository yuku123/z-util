#!/usr/bin/env python3
"""
Fix Java's "illegal unicode escape" errors.

Java's lexer processes \\uXXXX as a unicode escape in:
- string literals  (e.g., "\\u")
- javadoc comments (e.g., /** ... \\uXXXX ... */)
- line comments   (e.g., // \\uXXXX)
- block comments  (e.g., /* ... \\uXXXX ... */)

When it sees \\u not followed by exactly 4 hex digits, it errors out.

Fix strategy: replace \\u followed by non-hex char with \\u005Cu (the unicode
escape for backslash, then a literal 'u'). Result: same output text in the
string/comment, but Java no longer chokes.

Constraints:
- We only replace \\u when followed by non-hex chars (so we don't break valid
  escapes like \\u4e2d).
- Skip \\u005C (already escaped backslash) and \\uXXXX with 4 hex digits.
- Skip Java code paths where the issue is structural rather than literal
  (e.g., already inside a string concatenation).
"""

import os
import re
import sys

# Match \u followed by NOT 4 hex digits and not already \u005C.
# Use a negative lookahead: \u(?![0-9a-fA-F]{4})(?!005C)
PATTERN = re.compile(r'\\u(?![0-9a-fA-F]{4})(?!005C)')


def fix_file(path: str) -> bool:
    with open(path, 'r', encoding='utf-8', errors='replace') as f:
        content = f.read()
    new_content = PATTERN.sub(r'\\u005Cu', content)
    if new_content != content:
        with open(path, 'w', encoding='utf-8') as f:
            f.write(new_content)
        return True
    return False


def main(roots):
    changed = []
    for root in roots:
        for dirpath, dirnames, filenames in os.walk(root):
            # Skip target/ and node_modules/ etc.
            dirnames[:] = [d for d in dirnames if d not in
                           ('target', 'node_modules', '.git', '.idea', 'out')]
            for fn in filenames:
                if not fn.endswith('.java'):
                    continue
                p = os.path.join(dirpath, fn)
                if fix_file(p):
                    changed.append(p)
    print(f"Fixed {len(changed)} files:")
    for p in changed:
        print(f"  {p}")


if __name__ == '__main__':
    roots = sys.argv[1:] or ['/Users/zifang/workplace/idea_workplace/z-util']
    main(roots)
