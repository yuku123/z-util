package com.zifang.util.source.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.zifang.util.source.generator.info.ClassInfo;
import com.zifang.util.source.generator.info.FieldInfo;
import com.zifang.util.source.generator.info.MethodInfo;
import com.zifang.util.source.generator.info.MethodParameterPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 源代码解析器
 * <p>
 * 使用 JavaParser 将 Java 源代码解析为 ClassInfo 模型。
 * 支持从文件、InputStream 或字符串解析。
 *
 * @author zifang
 */
public class SourceCodeParser {

    private JavaParser javaParser;

    public SourceCodeParser() {
        ParserConfiguration config = new ParserConfiguration();
        config.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_8);
        this.javaParser = new JavaParser(config);
    }

    /**
     * 从文件路径解析 Java 源码
     */
    public ClassInfo parse(String filePath) throws IOException {
        return parse(new File(filePath));
    }

    /**
     * 从 File 对象解析 Java 源码
     */
    public ClassInfo parse(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return parse(fis);
        }
    }

    /**
     * 从 InputStream 解析 Java 源码
     */
    public ClassInfo parse(InputStream is) {
        CompilationUnit cu = javaParser.parse(is).getResult().orElseThrow(
                () -> new RuntimeException("Failed to parse Java source")
        );
        return convertToClassInfo(cu);
    }

    /**
     * 从字符串解析 Java 源码
     */
    public ClassInfo parseSource(String sourceCode) {
        CompilationUnit cu = javaParser.parse(sourceCode).getResult().orElseThrow(
                () -> new RuntimeException("Failed to parse Java source")
        );
        return convertToClassInfo(cu);
    }

    /**
     * 将 CompilationUnit 转换为 ClassInfo
     */
    private ClassInfo convertToClassInfo(CompilationUnit cu) {
        ClassInfo classInfo = new ClassInfo();

        // 包名
        classInfo.setPackageName(
                cu.getPackageDeclaration().map(p -> p.getName().asString()).orElse("")
        );

        // 类型声明（只取第一个，支持 ClassOrInterfaceDeclaration）
        List<TypeDeclaration<?>> types = cu.getTypes();
        if (types.isEmpty()) {
            throw new RuntimeException("No class or interface declaration found");
        }

        TypeDeclaration<?> type = types.get(0);

        // 只有 ClassOrInterfaceDeclaration 支持完整的类信息
        if (!(type instanceof ClassOrInterfaceDeclaration)) {
            throw new RuntimeException("Only ClassOrInterfaceDeclaration is supported");
        }

        ClassOrInterfaceDeclaration classDecl = (ClassOrInterfaceDeclaration) type;

        classInfo.setSimpleClassName(classDecl.getName().asString());
        classInfo.setInterfaceType(classDecl.isInterface());
        classInfo.setModifiers(parseModifiers(classDecl));

        // 父类
        List<ClassOrInterfaceType> extendedTypes = classDecl.getExtendedTypes();
        if (!extendedTypes.isEmpty()) {
            ClassInfo superClassInfo = new ClassInfo();
            superClassInfo.setSimpleClassName(extendedTypes.get(0).getName().asString());
            superClassInfo.setPackageName("");
            classInfo.setSuperClass(superClassInfo);
        }

        // 接口列表
        List<ClassInfo> interfaces = new ArrayList<>();
        for (ClassOrInterfaceType iface : classDecl.getImplementedTypes()) {
            ClassInfo ifaceInfo = new ClassInfo();
            ifaceInfo.setSimpleClassName(iface.getName().asString());
            ifaceInfo.setPackageName("");
            interfaces.add(ifaceInfo);
        }
        classInfo.setInterfaces(interfaces);

        // 字段列表
        List<FieldInfo> fieldInfos = new ArrayList<>();
        for (FieldDeclaration field : classDecl.getFields()) {
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setType(field.getVariables().get(0).getType().asString());
            fieldInfo.setValue(field.getVariables().get(0).getName().asString());

            int mod = parseModifiers(field);
            fieldInfo.setModifiers(new int[]{mod});

            // 初始值
            if (field.getVariables().get(0).getInitializer().isPresent()) {
                fieldInfo.setInitializer(field.getVariables().get(0).getInitializer().get().toString());
            } else {
                fieldInfo.setInitializer("null");
            }

            fieldInfos.add(fieldInfo);
        }
        classInfo.setFields(fieldInfos);

        // 方法列表
        List<MethodInfo> methodInfos = new ArrayList<>();
        for (MethodDeclaration method : classDecl.getMethods()) {
            MethodInfo methodInfo = new MethodInfo();
            methodInfo.setMethodName(method.getName().asString());
            methodInfo.setModifier(parseModifiers(method));
            methodInfo.setReturnType(method.getType().asString());

            // 参数
            List<MethodParameterPair> params = new ArrayList<>();
            for (Parameter param : method.getParameters()) {
                MethodParameterPair pair = new MethodParameterPair();
                pair.setParamType(param.getType().asString());
                pair.setParamName(param.getName().asString());
                params.add(pair);
            }
            methodInfo.setMethodParameterPairs(params);

            // 方法体语句
            List<String> statements = new ArrayList<>();
            method.getBody().ifPresent(body -> {
                body.getStatements().forEach(stmt -> statements.add(stmt.toString()));
            });
            methodInfo.setStatements(statements);

            methodInfos.add(methodInfo);
        }
        classInfo.setMethods(methodInfos);

        // 导入语句
        List<String> imports = new ArrayList<>();
        cu.getImports().forEach(imp -> imports.add(imp.getName().asString()));
        classInfo.setImports(imports);

        return classInfo;
    }

    /**
     * 解析修饰符
     */
    private int parseModifiers(com.github.javaparser.ast.body.BodyDeclaration<?> declaration) {
        int modifiers = 0;

        // 尝试从 ClassOrInterfaceDeclaration 获取
        if (declaration instanceof ClassOrInterfaceDeclaration) {
            for (Modifier mod : ((ClassOrInterfaceDeclaration) declaration).getModifiers()) {
                modifiers |= modifierKeywordToInt(mod.getKeyword().name());
            }
        }
        // 尝试从 MethodDeclaration 获取
        else if (declaration instanceof MethodDeclaration) {
            for (Modifier mod : ((MethodDeclaration) declaration).getModifiers()) {
                modifiers |= modifierKeywordToInt(mod.getKeyword().name());
            }
        }
        // 尝试从 FieldDeclaration 获取
        else if (declaration instanceof FieldDeclaration) {
            for (Modifier mod : ((FieldDeclaration) declaration).getModifiers()) {
                modifiers |= modifierKeywordToInt(mod.getKeyword().name());
            }
        }

        return modifiers;
    }

    /**
     * 将 javaparser Modifier.Keyword 名称转换为 java.lang.reflect.Modifier 常量
     */
    private int modifierKeywordToInt(String keywordName) {
        switch (keywordName) {
            case "PUBLIC": return java.lang.reflect.Modifier.PUBLIC;
            case "PRIVATE": return java.lang.reflect.Modifier.PRIVATE;
            case "PROTECTED": return java.lang.reflect.Modifier.PROTECTED;
            case "STATIC": return java.lang.reflect.Modifier.STATIC;
            case "FINAL": return java.lang.reflect.Modifier.FINAL;
            case "ABSTRACT": return java.lang.reflect.Modifier.ABSTRACT;
            case "SYNCHRONIZED": return java.lang.reflect.Modifier.SYNCHRONIZED;
            case "VOLATILE": return java.lang.reflect.Modifier.VOLATILE;
            case "TRANSIENT": return java.lang.reflect.Modifier.TRANSIENT;
            case "NATIVE": return java.lang.reflect.Modifier.NATIVE;
            case "STRICTFP": return java.lang.reflect.Modifier.STRICT;
            default: return 0;
        }
    }
}
