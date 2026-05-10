package com.zifang.util.core.system.properties;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

/**
 * @author zifang
 */
public class JvmProperties {

    private String awtToolkit;
    private String fileEncoding;
    private String fileEncodingPkg;
    private String fileSeparator;
    private String gopherProxySet;
    private String ideaTestCyclicBufferSize;
    private String javaAwtGraphicsenv;
    private String javaAwtPrinterjob;
    private String javaClassPath;
    private String javaClassVersion;
    private String javaEndorsedDirs;
    private String javaExtDirs;
    private String javaHome;
    private String javaIoTmpdir;
    private String javaLibraryPath;
    private String javaRuntimeName;
    private String javaRuntimeVersion;
    private String javaSpecificationName;
    private String javaSpecificationVendor;
    private String javaSpecificationVersion;
    private String javaVendor;
    private String javaVendorUrl;
    private String javaVendorUrlBug;
    private String javaVersion;
    private String javaVmInfo;
    private String javaVmName;
    private String javaVmSpecificationName;
    private String javaVmSpecificationVendor;
    private String javaVmSpecificationVersion;
    private String javaVmVendor;
    private String javaVmVersion;
    private String lineSeparator;
    private String osArch;
    private String osName;
    private String osVersion;
    private String pathSeparator;
    private String sunArchDataModel;
    private String sunBootClassPath;
    private String sunBootLibraryPath;
    private String sunCpuEndian;
    private String sunCpuIsalist;
    private String sunIoUnicodeEncoding;
    private String sunJavaCommand;
    private String sunJavaLauncher;
    private String sunJnuEncoding;
    private String sunManagementCompiler;
    private String sunOsPatchLevel;
    private String userCountry;
    private String userCountryFormat;
    private String userDir;
    private String userHome;
    private String userLanguage;
    private String userName;
    private String userTimezone;
    private String visualvmId;

    private Properties properties;

    public JvmProperties() {
        this.properties = System.getProperties();
    }

    public String getAwtToolkit() {
        return awtToolkit;
    }

    public void setAwtToolkit(String awtToolkit) {
        this.awtToolkit = awtToolkit;
    }

    public String getFileEncoding() {
        return fileEncoding;
    }

    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    public String getFileEncodingPkg() {
        return fileEncodingPkg;
    }

    public void setFileEncodingPkg(String fileEncodingPkg) {
        this.fileEncodingPkg = fileEncodingPkg;
    }

    public String getFileSeparator() {
        return fileSeparator;
    }

    public void setFileSeparator(String fileSeparator) {
        this.fileSeparator = fileSeparator;
    }

    public String getGopherProxySet() {
        return gopherProxySet;
    }

    public void setGopherProxySet(String gopherProxySet) {
        this.gopherProxySet = gopherProxySet;
    }

    public String getIdeaTestCyclicBufferSize() {
        return ideaTestCyclicBufferSize;
    }

    public void setIdeaTestCyclicBufferSize(String ideaTestCyclicBufferSize) {
        this.ideaTestCyclicBufferSize = ideaTestCyclicBufferSize;
    }

    public String getJavaAwtGraphicsenv() {
        return javaAwtGraphicsenv;
    }

    public void setJavaAwtGraphicsenv(String javaAwtGraphicsenv) {
        this.javaAwtGraphicsenv = javaAwtGraphicsenv;
    }

    public String getJavaAwtPrinterjob() {
        return javaAwtPrinterjob;
    }

    public void setJavaAwtPrinterjob(String javaAwtPrinterjob) {
        this.javaAwtPrinterjob = javaAwtPrinterjob;
    }

    public String getJavaClassPath() {
        return javaClassPath;
    }

    public void setJavaClassPath(String javaClassPath) {
        this.javaClassPath = javaClassPath;
    }

    public String getJavaClassVersion() {
        return javaClassVersion;
    }

    public void setJavaClassVersion(String javaClassVersion) {
        this.javaClassVersion = javaClassVersion;
    }

    public String getJavaEndorsedDirs() {
        return javaEndorsedDirs;
    }

    public void setJavaEndorsedDirs(String javaEndorsedDirs) {
        this.javaEndorsedDirs = javaEndorsedDirs;
    }

    public String getJavaExtDirs() {
        return javaExtDirs;
    }

    public void setJavaExtDirs(String javaExtDirs) {
        this.javaExtDirs = javaExtDirs;
    }

    public String getJavaHome() {
        return javaHome;
    }

    public void setJavaHome(String javaHome) {
        this.javaHome = javaHome;
    }

    public String getJavaIoTmpdir() {
        return javaIoTmpdir;
    }

    public void setJavaIoTmpdir(String javaIoTmpdir) {
        this.javaIoTmpdir = javaIoTmpdir;
    }

    public String getJavaLibraryPath() {
        return javaLibraryPath;
    }

    public void setJavaLibraryPath(String javaLibraryPath) {
        this.javaLibraryPath = javaLibraryPath;
    }

    public String getJavaRuntimeName() {
        return javaRuntimeName;
    }

    public void setJavaRuntimeName(String javaRuntimeName) {
        this.javaRuntimeName = javaRuntimeName;
    }

    public String getJavaRuntimeVersion() {
        return javaRuntimeVersion;
    }

    public void setJavaRuntimeVersion(String javaRuntimeVersion) {
        this.javaRuntimeVersion = javaRuntimeVersion;
    }

    public String getJavaSpecificationName() {
        return javaSpecificationName;
    }

    public void setJavaSpecificationName(String javaSpecificationName) {
        this.javaSpecificationName = javaSpecificationName;
    }

    public String getJavaSpecificationVendor() {
        return javaSpecificationVendor;
    }

    public void setJavaSpecificationVendor(String javaSpecificationVendor) {
        this.javaSpecificationVendor = javaSpecificationVendor;
    }

    public String getJavaSpecificationVersion() {
        return javaSpecificationVersion;
    }

    public void setJavaSpecificationVersion(String javaSpecificationVersion) {
        this.javaSpecificationVersion = javaSpecificationVersion;
    }

    public String getJavaVendor() {
        return javaVendor;
    }

    public void setJavaVendor(String javaVendor) {
        this.javaVendor = javaVendor;
    }

    public String getJavaVendorUrl() {
        return javaVendorUrl;
    }

    public void setJavaVendorUrl(String javaVendorUrl) {
        this.javaVendorUrl = javaVendorUrl;
    }

    public String getJavaVendorUrlBug() {
        return javaVendorUrlBug;
    }

    public void setJavaVendorUrlBug(String javaVendorUrlBug) {
        this.javaVendorUrlBug = javaVendorUrlBug;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public String getJavaVmInfo() {
        return javaVmInfo;
    }

    public void setJavaVmInfo(String javaVmInfo) {
        this.javaVmInfo = javaVmInfo;
    }

    public String getJavaVmName() {
        return javaVmName;
    }

    public void setJavaVmName(String javaVmName) {
        this.javaVmName = javaVmName;
    }

    public String getJavaVmSpecificationName() {
        return javaVmSpecificationName;
    }

    public void setJavaVmSpecificationName(String javaVmSpecificationName) {
        this.javaVmSpecificationName = javaVmSpecificationName;
    }

    public String getJavaVmSpecificationVendor() {
        return javaVmSpecificationVendor;
    }

    public void setJavaVmSpecificationVendor(String javaVmSpecificationVendor) {
        this.javaVmSpecificationVendor = javaVmSpecificationVendor;
    }

    public String getJavaVmSpecificationVersion() {
        return javaVmSpecificationVersion;
    }

    public void setJavaVmSpecificationVersion(String javaVmSpecificationVersion) {
        this.javaVmSpecificationVersion = javaVmSpecificationVersion;
    }

    public String getJavaVmVendor() {
        return javaVmVendor;
    }

    public void setJavaVmVendor(String javaVmVendor) {
        this.javaVmVendor = javaVmVendor;
    }

    public String getJavaVmVersion() {
        return javaVmVersion;
    }

    public void setJavaVmVersion(String javaVmVersion) {
        this.javaVmVersion = javaVmVersion;
    }

    public String getLineSeparator() {
        return lineSeparator;
    }

    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    public String getOsArch() {
        return osArch;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getPathSeparator() {
        return pathSeparator;
    }

    public void setPathSeparator(String pathSeparator) {
        this.pathSeparator = pathSeparator;
    }

    public String getSunArchDataModel() {
        return sunArchDataModel;
    }

    public void setSunArchDataModel(String sunArchDataModel) {
        this.sunArchDataModel = sunArchDataModel;
    }

    public String getSunBootClassPath() {
        return sunBootClassPath;
    }

    public void setSunBootClassPath(String sunBootClassPath) {
        this.sunBootClassPath = sunBootClassPath;
    }

    public String getSunBootLibraryPath() {
        return sunBootLibraryPath;
    }

    public void setSunBootLibraryPath(String sunBootLibraryPath) {
        this.sunBootLibraryPath = sunBootLibraryPath;
    }

    public String getSunCpuEndian() {
        return sunCpuEndian;
    }

    public void setSunCpuEndian(String sunCpuEndian) {
        this.sunCpuEndian = sunCpuEndian;
    }

    public String getSunCpuIsalist() {
        return sunCpuIsalist;
    }

    public void setSunCpuIsalist(String sunCpuIsalist) {
        this.sunCpuIsalist = sunCpuIsalist;
    }

    public String getSunIoUnicodeEncoding() {
        return sunIoUnicodeEncoding;
    }

    public void setSunIoUnicodeEncoding(String sunIoUnicodeEncoding) {
        this.sunIoUnicodeEncoding = sunIoUnicodeEncoding;
    }

    public String getSunJavaCommand() {
        return sunJavaCommand;
    }

    public void setSunJavaCommand(String sunJavaCommand) {
        this.sunJavaCommand = sunJavaCommand;
    }

    public String getSunJavaLauncher() {
        return sunJavaLauncher;
    }

    public void setSunJavaLauncher(String sunJavaLauncher) {
        this.sunJavaLauncher = sunJavaLauncher;
    }

    public String getSunJnuEncoding() {
        return sunJnuEncoding;
    }

    public void setSunJnuEncoding(String sunJnuEncoding) {
        this.sunJnuEncoding = sunJnuEncoding;
    }

    public String getSunManagementCompiler() {
        return sunManagementCompiler;
    }

    public void setSunManagementCompiler(String sunManagementCompiler) {
        this.sunManagementCompiler = sunManagementCompiler;
    }

    public String getSunOsPatchLevel() {
        return sunOsPatchLevel;
    }

    public void setSunOsPatchLevel(String sunOsPatchLevel) {
        this.sunOsPatchLevel = sunOsPatchLevel;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getUserCountryFormat() {
        return userCountryFormat;
    }

    public void setUserCountryFormat(String userCountryFormat) {
        this.userCountryFormat = userCountryFormat;
    }

    public String getUserDir() {
        return userDir;
    }

    public void setUserDir(String userDir) {
        this.userDir = userDir;
    }

    public String getUserHome() {
        return userHome;
    }

    public void setUserHome(String userHome) {
        this.userHome = userHome;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTimezone() {
        return userTimezone;
    }

    public void setUserTimezone(String userTimezone) {
        this.userTimezone = userTimezone;
    }

    public String getVisualvmId() {
        return visualvmId;
    }

    public void setVisualvmId(String visualvmId) {
        this.visualvmId = visualvmId;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void init() {
        for (Map.Entry<String, String> entry : JvmPropertiesDefine.defineMap.entrySet()) {
            try {
                Field field = this.getClass().getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(this, properties.getProperty(entry.getValue()));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "JvmProperties{awtToolkit=" + awtToolkit + ", fileEncoding=" + fileEncoding +
                ", fileEncodingPkg=" + fileEncodingPkg + ", fileSeparator=" + fileSeparator +
                ", gopherProxySet=" + gopherProxySet + ", ideaTestCyclicBufferSize=" + ideaTestCyclicBufferSize +
                ", javaAwtGraphicsenv=" + javaAwtGraphicsenv + ", javaAwtPrinterjob=" + javaAwtPrinterjob +
                ", javaClassPath=" + javaClassPath + ", javaClassVersion=" + javaClassVersion +
                ", javaEndorsedDirs=" + javaEndorsedDirs + ", javaExtDirs=" + javaExtDirs +
                ", javaHome=" + javaHome + ", javaIoTmpdir=" + javaIoTmpdir +
                ", javaLibraryPath=" + javaLibraryPath + ", javaRuntimeName=" + javaRuntimeName +
                ", javaRuntimeVersion=" + javaRuntimeVersion + ", javaSpecificationName=" + javaSpecificationName +
                ", javaSpecificationVendor=" + javaSpecificationVendor + ", javaSpecificationVersion=" + javaSpecificationVersion +
                ", javaVendor=" + javaVendor + ", javaVendorUrl=" + javaVendorUrl +
                ", javaVendorUrlBug=" + javaVendorUrlBug + ", javaVersion=" + javaVersion +
                ", javaVmInfo=" + javaVmInfo + ", javaVmName=" + javaVmName +
                ", javaVmSpecificationName=" + javaVmSpecificationName + ", javaVmSpecificationVendor=" + javaVmSpecificationVendor +
                ", javaVmSpecificationVersion=" + javaVmSpecificationVersion + ", javaVmVendor=" + javaVmVendor +
                ", javaVmVersion=" + javaVmVersion + ", lineSeparator=" + lineSeparator +
                ", osArch=" + osArch + ", osName=" + osName + ", osVersion=" + osVersion +
                ", pathSeparator=" + pathSeparator + ", sunArchDataModel=" + sunArchDataModel +
                ", sunBootClassPath=" + sunBootClassPath + ", sunBootLibraryPath=" + sunBootLibraryPath +
                ", sunCpuEndian=" + sunCpuEndian + ", sunCpuIsalist=" + sunCpuIsalist +
                ", sunIoUnicodeEncoding=" + sunIoUnicodeEncoding + ", sunJavaCommand=" + sunJavaCommand +
                ", sunJavaLauncher=" + sunJavaLauncher + ", sunJnuEncoding=" + sunJnuEncoding +
                ", sunManagementCompiler=" + sunManagementCompiler + ", sunOsPatchLevel=" + sunOsPatchLevel +
                ", userCountry=" + userCountry + ", userCountryFormat=" + userCountryFormat +
                ", userDir=" + userDir + ", userHome=" + userHome +
                ", userLanguage=" + userLanguage + ", userName=" + userName +
                ", userTimezone=" + userTimezone + ", visualvmId=" + visualvmId + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JvmProperties that = (JvmProperties) o;
        return java.util.Objects.equals(awtToolkit, that.awtToolkit) &&
                java.util.Objects.equals(fileEncoding, that.fileEncoding) &&
                java.util.Objects.equals(fileEncodingPkg, that.fileEncodingPkg) &&
                java.util.Objects.equals(fileSeparator, that.fileSeparator) &&
                java.util.Objects.equals(gopherProxySet, that.gopherProxySet) &&
                java.util.Objects.equals(ideaTestCyclicBufferSize, that.ideaTestCyclicBufferSize) &&
                java.util.Objects.equals(javaAwtGraphicsenv, that.javaAwtGraphicsenv) &&
                java.util.Objects.equals(javaAwtPrinterjob, that.javaAwtPrinterjob) &&
                java.util.Objects.equals(javaClassPath, that.javaClassPath) &&
                java.util.Objects.equals(javaClassVersion, that.javaClassVersion) &&
                java.util.Objects.equals(javaEndorsedDirs, that.javaEndorsedDirs) &&
                java.util.Objects.equals(javaExtDirs, that.javaExtDirs) &&
                java.util.Objects.equals(javaHome, that.javaHome) &&
                java.util.Objects.equals(javaIoTmpdir, that.javaIoTmpdir) &&
                java.util.Objects.equals(javaLibraryPath, that.javaLibraryPath) &&
                java.util.Objects.equals(javaRuntimeName, that.javaRuntimeName) &&
                java.util.Objects.equals(javaRuntimeVersion, that.javaRuntimeVersion) &&
                java.util.Objects.equals(javaSpecificationName, that.javaSpecificationName) &&
                java.util.Objects.equals(javaSpecificationVendor, that.javaSpecificationVendor) &&
                java.util.Objects.equals(javaSpecificationVersion, that.javaSpecificationVersion) &&
                java.util.Objects.equals(javaVendor, that.javaVendor) &&
                java.util.Objects.equals(javaVendorUrl, that.javaVendorUrl) &&
                java.util.Objects.equals(javaVendorUrlBug, that.javaVendorUrlBug) &&
                java.util.Objects.equals(javaVersion, that.javaVersion) &&
                java.util.Objects.equals(javaVmInfo, that.javaVmInfo) &&
                java.util.Objects.equals(javaVmName, that.javaVmName) &&
                java.util.Objects.equals(javaVmSpecificationName, that.javaVmSpecificationName) &&
                java.util.Objects.equals(javaVmSpecificationVendor, that.javaVmSpecificationVendor) &&
                java.util.Objects.equals(javaVmSpecificationVersion, that.javaVmSpecificationVersion) &&
                java.util.Objects.equals(javaVmVendor, that.javaVmVendor) &&
                java.util.Objects.equals(javaVmVersion, that.javaVmVersion) &&
                java.util.Objects.equals(lineSeparator, that.lineSeparator) &&
                java.util.Objects.equals(osArch, that.osArch) &&
                java.util.Objects.equals(osName, that.osName) &&
                java.util.Objects.equals(osVersion, that.osVersion) &&
                java.util.Objects.equals(pathSeparator, that.pathSeparator) &&
                java.util.Objects.equals(sunArchDataModel, that.sunArchDataModel) &&
                java.util.Objects.equals(sunBootClassPath, that.sunBootClassPath) &&
                java.util.Objects.equals(sunBootLibraryPath, that.sunBootLibraryPath) &&
                java.util.Objects.equals(sunCpuEndian, that.sunCpuEndian) &&
                java.util.Objects.equals(sunCpuIsalist, that.sunCpuIsalist) &&
                java.util.Objects.equals(sunIoUnicodeEncoding, that.sunIoUnicodeEncoding) &&
                java.util.Objects.equals(sunJavaCommand, that.sunJavaCommand) &&
                java.util.Objects.equals(sunJavaLauncher, that.sunJavaLauncher) &&
                java.util.Objects.equals(sunJnuEncoding, that.sunJnuEncoding) &&
                java.util.Objects.equals(sunManagementCompiler, that.sunManagementCompiler) &&
                java.util.Objects.equals(sunOsPatchLevel, that.sunOsPatchLevel) &&
                java.util.Objects.equals(userCountry, that.userCountry) &&
                java.util.Objects.equals(userCountryFormat, that.userCountryFormat) &&
                java.util.Objects.equals(userDir, that.userDir) &&
                java.util.Objects.equals(userHome, that.userHome) &&
                java.util.Objects.equals(userLanguage, that.userLanguage) &&
                java.util.Objects.equals(userName, that.userName) &&
                java.util.Objects.equals(userTimezone, that.userTimezone) &&
                java.util.Objects.equals(visualvmId, that.visualvmId) &&
                java.util.Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(awtToolkit, fileEncoding, fileEncodingPkg, fileSeparator,
                gopherProxySet, ideaTestCyclicBufferSize, javaAwtGraphicsenv, javaAwtPrinterjob,
                javaClassPath, javaClassVersion, javaEndorsedDirs, javaExtDirs, javaHome,
                javaIoTmpdir, javaLibraryPath, javaRuntimeName, javaRuntimeVersion,
                javaSpecificationName, javaSpecificationVendor, javaSpecificationVersion,
                javaVendor, javaVendorUrl, javaVendorUrlBug, javaVersion, javaVmInfo,
                javaVmName, javaVmSpecificationName, javaVmSpecificationVendor, javaVmSpecificationVersion,
                javaVmVendor, javaVmVersion, lineSeparator, osArch, osName, osVersion,
                pathSeparator, sunArchDataModel, sunBootClassPath, sunBootLibraryPath,
                sunCpuEndian, sunCpuIsalist, sunIoUnicodeEncoding, sunJavaCommand,
                sunJavaLauncher, sunJnuEncoding, sunManagementCompiler, sunOsPatchLevel,
                userCountry, userCountryFormat, userDir, userHome, userLanguage,
                userName, userTimezone, visualvmId, properties);
    }
}