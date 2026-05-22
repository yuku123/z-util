package com.zifang.util.compile.sorce2.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.zifang.util.compile.sorce2.generator.info.ClassInfo;
import com.zifang.util.compile.sorce2.generator.info.FieldInfo;
import com.zifang.util.compile.sorce2.generator.info.MethodInfo;
import com.zifang.util.compile.sorce2.generator.info.MethodParameterPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 源代码解析器
 * <p>
 * 使用 JavaParser 将 Java 源代码解析为 ClassInfo 模型。
 * 支持从文件、路径、InputStream 或字符串解析。
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
        Optional<String> packageName = cu.getPackageDeclaration().map(p -> p.getName().asString());
        classInfo.setPackageName(packageName.orElse(""));

        // 类型声明（类或接口）
        List<TypeDeclaration<?>> types = cu.getTypes();
        if (types.isEmpty()) {
            throw new RuntimeException("No class or interface declaration found");
        }

        TypeDeclaration<?> type = types.get(0);
        classInfo.setSimpleClassName(type.getName().asString());
        classInfo.setInterfaceType(type.isInterface());
        classInfo.setModifiers(parseModifiers(type));

        // 父类
        if (type.getExtendedTypes(0).isPresent()) {
            ClassOrInterfaceType superClass = type.getExtendedTypes(0).get();
            ClassInfo superClassInfo = new ClassInfo();
            superClassInfo.setSimpleClassName(superClass.getName().asString());
            superClassInfo.setPackageName("");
            classInfo.setSuperClass(superClassInfo);
        }

        // 接口列表
        List<ClassInfo> interfaces = new ArrayList<>();
        for (ClassOrInterfaceType iface : type.getImplementedTypes()) {
            ClassInfo ifaceInfo = new ClassInfo();
            ifaceInfo.setSimpleClassName(iface.getName().asString());
            ifaceInfo.setPackageName("");
            interfaces.add(ifaceInfo);
        }
        classInfo.setInterfaces(interfaces);

        // 字段列表
        List<FieldInfo> fieldInfos = new ArrayList<>();
        for (FieldDeclaration field : type.getFields()) {
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setType(typeToString(field.getVariables().get(0).getType()));
            fieldInfo.setValue(field.getVariables().get(0).getName().asString());
            fieldInfo.setModifiers(parseModifiers(field));

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
        for (MethodDeclaration method : type.getMethods()) {
            MethodInfo methodInfo = new MethodInfo();
            methodInfo.setMethodName(method.getName().asString());
            methodInfo.setModifier(parseModifiers(method));
            methodInfo.setReturnType(typeToString(method.getType()));

            // 参数
            List<MethodParameterPair> params = new ArrayList<>();
            for (Parameter param : method.getParameters()) {
                MethodParameterPair pair = new MethodParameterPair();
                pair.setParamType(typeToString(param.getType()));
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
        for (com.github.javaparser.ast.Modifier mod : declaration.getModifiers()) {
            modifiers |= modifierKeywordToInt(mod.getKeyword());
        }
        return modifiers;
    }

    /**
     * 将 javaparser Modifier.Keyword 转换为 java.lang.reflect.Modifier 常量
     */
    private int modifierKeywordToInt(com.github.javaparser.ast.Modifier.Keyword keyword) {
        switch (keyword) {
            case PUBLIC: return java.lang.reflect.Modifier.PUBLIC;
            case PRIVATE: return java.lang.reflect.Modifier.PRIVATE;
            case PROTECTED: return java.lang.reflect.Modifier.PROTECTED;
            case STATIC: return java.lang.reflect.Modifier.STATIC;
            case FINAL: return java.lang.reflect.Modifier.FINAL;
            case ABSTRACT: return java.lang.reflect.Modifier.ABSTRACT;
            case SYNCHRONIZED: return java.lang.reflect.Modifier.SYNCHRONIZED;
            case VOLATILE: return java.lang.reflect.Modifier.VOLATILE;
            case TRANSIENT: return java.lang.reflect.Modifier.TRANSIENT;
            case NATIVE: return java.lang.reflect.Modifier.NATIVE;
            case STRICTFP: return java.lang.reflect.Modifier.STRICT;
            case SYNTHETIC: return 0x00001000; // Modifier 没有 SYNTHETIC，直接用值
            default: return 0;
        }
    }

    /**
     * 将 JavaParser Type 转换为字符串类型
     */
    private String typeToString(Type type) {
        if (type instanceof PrimitiveType) {
            return type.asString();
        } else if (type instanceof ReferenceType) {
            return ((ReferenceType) type).asString();
        } else if (type instanceof ClassOrInterfaceType) {
            return type.asString();
        }
        return type.asString();
    }
}
