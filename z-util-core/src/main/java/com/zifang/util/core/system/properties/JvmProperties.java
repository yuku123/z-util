package com.zifang.util.core.system.properties;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

/**
 * @author zifang
 */
/**
 * JvmProperties类。
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

    /**
     * JvmProperties方法。
     */
    public JvmProperties() {
        this.properties = System.getProperties();
    }

    /**
     * getAwtToolkit方法。
     * @return String类型返回值
     */
    public String getAwtToolkit() {
        return awtToolkit;
    }

    /**
     * setAwtToolkit方法。
     *      * @param awtToolkit String类型参数
     */
    public void setAwtToolkit(String awtToolkit) {
        this.awtToolkit = awtToolkit;
    }

    /**
     * getFileEncoding方法。
     * @return String类型返回值
     */
    public String getFileEncoding() {
        return fileEncoding;
    }

    /**
     * setFileEncoding方法。
     *      * @param fileEncoding String类型参数
     */
    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    /**
     * getFileEncodingPkg方法。
     * @return String类型返回值
     */
    public String getFileEncodingPkg() {
        return fileEncodingPkg;
    }

    /**
     * setFileEncodingPkg方法。
     *      * @param fileEncodingPkg String类型参数
     */
    public void setFileEncodingPkg(String fileEncodingPkg) {
        this.fileEncodingPkg = fileEncodingPkg;
    }

    /**
     * getFileSeparator方法。
     * @return String类型返回值
     */
    public String getFileSeparator() {
        return fileSeparator;
    }

    /**
     * setFileSeparator方法。
     *      * @param fileSeparator String类型参数
     */
    public void setFileSeparator(String fileSeparator) {
        this.fileSeparator = fileSeparator;
    }

    /**
     * getGopherProxySet方法。
     * @return String类型返回值
     */
    public String getGopherProxySet() {
        return gopherProxySet;
    }

    /**
     * setGopherProxySet方法。
     *      * @param gopherProxySet String类型参数
     */
    public void setGopherProxySet(String gopherProxySet) {
        this.gopherProxySet = gopherProxySet;
    }

    /**
     * getIdeaTestCyclicBufferSize方法。
     * @return String类型返回值
     */
    public String getIdeaTestCyclicBufferSize() {
        return ideaTestCyclicBufferSize;
    }

    /**
     * setIdeaTestCyclicBufferSize方法。
     *      * @param ideaTestCyclicBufferSize String类型参数
     */
    public void setIdeaTestCyclicBufferSize(String ideaTestCyclicBufferSize) {
        this.ideaTestCyclicBufferSize = ideaTestCyclicBufferSize;
    }

    /**
     * getJavaAwtGraphicsenv方法。
     * @return String类型返回值
     */
    public String getJavaAwtGraphicsenv() {
        return javaAwtGraphicsenv;
    }

    /**
     * setJavaAwtGraphicsenv方法。
     *      * @param javaAwtGraphicsenv String类型参数
     */
    public void setJavaAwtGraphicsenv(String javaAwtGraphicsenv) {
        this.javaAwtGraphicsenv = javaAwtGraphicsenv;
    }

    /**
     * getJavaAwtPrinterjob方法。
     * @return String类型返回值
     */
    public String getJavaAwtPrinterjob() {
        return javaAwtPrinterjob;
    }

    /**
     * setJavaAwtPrinterjob方法。
     *      * @param javaAwtPrinterjob String类型参数
     */
    public void setJavaAwtPrinterjob(String javaAwtPrinterjob) {
        this.javaAwtPrinterjob = javaAwtPrinterjob;
    }

    /**
     * getJavaClassPath方法。
     * @return String类型返回值
     */
    public String getJavaClassPath() {
        return javaClassPath;
    }

    /**
     * setJavaClassPath方法。
     *      * @param javaClassPath String类型参数
     */
    public void setJavaClassPath(String javaClassPath) {
        this.javaClassPath = javaClassPath;
    }

    /**
     * getJavaClassVersion方法。
     * @return String类型返回值
     */
    public String getJavaClassVersion() {
        return javaClassVersion;
    }

    /**
     * setJavaClassVersion方法。
     *      * @param javaClassVersion String类型参数
     */
    public void setJavaClassVersion(String javaClassVersion) {
        this.javaClassVersion = javaClassVersion;
    }

    /**
     * getJavaEndorsedDirs方法。
     * @return String类型返回值
     */
    public String getJavaEndorsedDirs() {
        return javaEndorsedDirs;
    }

    /**
     * setJavaEndorsedDirs方法。
     *      * @param javaEndorsedDirs String类型参数
     */
    public void setJavaEndorsedDirs(String javaEndorsedDirs) {
        this.javaEndorsedDirs = javaEndorsedDirs;
    }

    /**
     * getJavaExtDirs方法。
     * @return String类型返回值
     */
    public String getJavaExtDirs() {
        return javaExtDirs;
    }

    /**
     * setJavaExtDirs方法。
     *      * @param javaExtDirs String类型参数
     */
    public void setJavaExtDirs(String javaExtDirs) {
        this.javaExtDirs = javaExtDirs;
    }

    /**
     * getJavaHome方法。
     * @return String类型返回值
     */
    public String getJavaHome() {
        return javaHome;
    }

    /**
     * setJavaHome方法。
     *      * @param javaHome String类型参数
     */
    public void setJavaHome(String javaHome) {
        this.javaHome = javaHome;
    }

    /**
     * getJavaIoTmpdir方法。
     * @return String类型返回值
     */
    public String getJavaIoTmpdir() {
        return javaIoTmpdir;
    }

    /**
     * setJavaIoTmpdir方法。
     *      * @param javaIoTmpdir String类型参数
     */
    public void setJavaIoTmpdir(String javaIoTmpdir) {
        this.javaIoTmpdir = javaIoTmpdir;
    }

    /**
     * getJavaLibraryPath方法。
     * @return String类型返回值
     */
    public String getJavaLibraryPath() {
        return javaLibraryPath;
    }

    /**
     * setJavaLibraryPath方法。
     *      * @param javaLibraryPath String类型参数
     */
    public void setJavaLibraryPath(String javaLibraryPath) {
        this.javaLibraryPath = javaLibraryPath;
    }

    /**
     * getJavaRuntimeName方法。
     * @return String类型返回值
     */
    public String getJavaRuntimeName() {
        return javaRuntimeName;
    }

    /**
     * setJavaRuntimeName方法。
     *      * @param javaRuntimeName String类型参数
     */
    public void setJavaRuntimeName(String javaRuntimeName) {
        this.javaRuntimeName = javaRuntimeName;
    }

    /**
     * getJavaRuntimeVersion方法。
     * @return String类型返回值
     */
    public String getJavaRuntimeVersion() {
        return javaRuntimeVersion;
    }

    /**
     * setJavaRuntimeVersion方法。
     *      * @param javaRuntimeVersion String类型参数
     */
    public void setJavaRuntimeVersion(String javaRuntimeVersion) {
        this.javaRuntimeVersion = javaRuntimeVersion;
    }

    /**
     * getJavaSpecificationName方法。
     * @return String类型返回值
     */
    public String getJavaSpecificationName() {
        return javaSpecificationName;
    }

    /**
     * setJavaSpecificationName方法。
     *      * @param javaSpecificationName String类型参数
     */
    public void setJavaSpecificationName(String javaSpecificationName) {
        this.javaSpecificationName = javaSpecificationName;
    }

    /**
     * getJavaSpecificationVendor方法。
     * @return String类型返回值
     */
    public String getJavaSpecificationVendor() {
        return javaSpecificationVendor;
    }

    /**
     * setJavaSpecificationVendor方法。
     *      * @param javaSpecificationVendor String类型参数
     */
    public void setJavaSpecificationVendor(String javaSpecificationVendor) {
        this.javaSpecificationVendor = javaSpecificationVendor;
    }

    /**
     * getJavaSpecificationVersion方法。
     * @return String类型返回值
     */
    public String getJavaSpecificationVersion() {
        return javaSpecificationVersion;
    }

    /**
     * setJavaSpecificationVersion方法。
     *      * @param javaSpecificationVersion String类型参数
     */
    public void setJavaSpecificationVersion(String javaSpecificationVersion) {
        this.javaSpecificationVersion = javaSpecificationVersion;
    }

    /**
     * getJavaVendor方法。
     * @return String类型返回值
     */
    public String getJavaVendor() {
        return javaVendor;
    }

    /**
     * setJavaVendor方法。
     *      * @param javaVendor String类型参数
     */
    public void setJavaVendor(String javaVendor) {
        this.javaVendor = javaVendor;
    }

    /**
     * getJavaVendorUrl方法。
     * @return String类型返回值
     */
    public String getJavaVendorUrl() {
        return javaVendorUrl;
    }

    /**
     * setJavaVendorUrl方法。
     *      * @param javaVendorUrl String类型参数
     */
    public void setJavaVendorUrl(String javaVendorUrl) {
        this.javaVendorUrl = javaVendorUrl;
    }

    /**
     * getJavaVendorUrlBug方法。
     * @return String类型返回值
     */
    public String getJavaVendorUrlBug() {
        return javaVendorUrlBug;
    }

    /**
     * setJavaVendorUrlBug方法。
     *      * @param javaVendorUrlBug String类型参数
     */
    public void setJavaVendorUrlBug(String javaVendorUrlBug) {
        this.javaVendorUrlBug = javaVendorUrlBug;
    }

    /**
     * getJavaVersion方法。
     * @return String类型返回值
     */
    public String getJavaVersion() {
        return javaVersion;
    }

    /**
     * setJavaVersion方法。
     *      * @param javaVersion String类型参数
     */
    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    /**
     * getJavaVmInfo方法。
     * @return String类型返回值
     */
    public String getJavaVmInfo() {
        return javaVmInfo;
    }

    /**
     * setJavaVmInfo方法。
     *      * @param javaVmInfo String类型参数
     */
    public void setJavaVmInfo(String javaVmInfo) {
        this.javaVmInfo = javaVmInfo;
    }

    /**
     * getJavaVmName方法。
     * @return String类型返回值
     */
    public String getJavaVmName() {
        return javaVmName;
    }

    /**
     * setJavaVmName方法。
     *      * @param javaVmName String类型参数
     */
    public void setJavaVmName(String javaVmName) {
        this.javaVmName = javaVmName;
    }

    /**
     * getJavaVmSpecificationName方法。
     * @return String类型返回值
     */
    public String getJavaVmSpecificationName() {
        return javaVmSpecificationName;
    }

    /**
     * setJavaVmSpecificationName方法。
     *      * @param javaVmSpecificationName String类型参数
     */
    public void setJavaVmSpecificationName(String javaVmSpecificationName) {
        this.javaVmSpecificationName = javaVmSpecificationName;
    }

    /**
     * getJavaVmSpecificationVendor方法。
     * @return String类型返回值
     */
    public String getJavaVmSpecificationVendor() {
        return javaVmSpecificationVendor;
    }

    /**
     * setJavaVmSpecificationVendor方法。
     *      * @param javaVmSpecificationVendor String类型参数
     */
    public void setJavaVmSpecificationVendor(String javaVmSpecificationVendor) {
        this.javaVmSpecificationVendor = javaVmSpecificationVendor;
    }

    /**
     * getJavaVmSpecificationVersion方法。
     * @return String类型返回值
     */
    public String getJavaVmSpecificationVersion() {
        return javaVmSpecificationVersion;
    }

    /**
     * setJavaVmSpecificationVersion方法。
     *      * @param javaVmSpecificationVersion String类型参数
     */
    public void setJavaVmSpecificationVersion(String javaVmSpecificationVersion) {
        this.javaVmSpecificationVersion = javaVmSpecificationVersion;
    }

    /**
     * getJavaVmVendor方法。
     * @return String类型返回值
     */
    public String getJavaVmVendor() {
        return javaVmVendor;
    }

    /**
     * setJavaVmVendor方法。
     *      * @param javaVmVendor String类型参数
     */
    public void setJavaVmVendor(String javaVmVendor) {
        this.javaVmVendor = javaVmVendor;
    }

    /**
     * getJavaVmVersion方法。
     * @return String类型返回值
     */
    public String getJavaVmVersion() {
        return javaVmVersion;
    }

    /**
     * setJavaVmVersion方法。
     *      * @param javaVmVersion String类型参数
     */
    public void setJavaVmVersion(String javaVmVersion) {
        this.javaVmVersion = javaVmVersion;
    }

    /**
     * getLineSeparator方法。
     * @return String类型返回值
     */
    public String getLineSeparator() {
        return lineSeparator;
    }

    /**
     * setLineSeparator方法。
     *      * @param lineSeparator String类型参数
     */
    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    /**
     * getOsArch方法。
     * @return String类型返回值
     */
    public String getOsArch() {
        return osArch;
    }

    /**
     * setOsArch方法。
     *      * @param osArch String类型参数
     */
    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    /**
     * getOsName方法。
     * @return String类型返回值
     */
    public String getOsName() {
        return osName;
    }

    /**
     * setOsName方法。
     *      * @param osName String类型参数
     */
    public void setOsName(String osName) {
        this.osName = osName;
    }

    /**
     * getOsVersion方法。
     * @return String类型返回值
     */
    public String getOsVersion() {
        return osVersion;
    }

    /**
     * setOsVersion方法。
     *      * @param osVersion String类型参数
     */
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    /**
     * getPathSeparator方法。
     * @return String类型返回值
     */
    public String getPathSeparator() {
        return pathSeparator;
    }

    /**
     * setPathSeparator方法。
     *      * @param pathSeparator String类型参数
     */
    public void setPathSeparator(String pathSeparator) {
        this.pathSeparator = pathSeparator;
    }

    /**
     * getSunArchDataModel方法。
     * @return String类型返回值
     */
    public String getSunArchDataModel() {
        return sunArchDataModel;
    }

    /**
     * setSunArchDataModel方法。
     *      * @param sunArchDataModel String类型参数
     */
    public void setSunArchDataModel(String sunArchDataModel) {
        this.sunArchDataModel = sunArchDataModel;
    }

    /**
     * getSunBootClassPath方法。
     * @return String类型返回值
     */
    public String getSunBootClassPath() {
        return sunBootClassPath;
    }

    /**
     * setSunBootClassPath方法。
     *      * @param sunBootClassPath String类型参数
     */
    public void setSunBootClassPath(String sunBootClassPath) {
        this.sunBootClassPath = sunBootClassPath;
    }

    /**
     * getSunBootLibraryPath方法。
     * @return String类型返回值
     */
    public String getSunBootLibraryPath() {
        return sunBootLibraryPath;
    }

    /**
     * setSunBootLibraryPath方法。
     *      * @param sunBootLibraryPath String类型参数
     */
    public void setSunBootLibraryPath(String sunBootLibraryPath) {
        this.sunBootLibraryPath = sunBootLibraryPath;
    }

    /**
     * getSunCpuEndian方法。
     * @return String类型返回值
     */
    public String getSunCpuEndian() {
        return sunCpuEndian;
    }

    /**
     * setSunCpuEndian方法。
     *      * @param sunCpuEndian String类型参数
     */
    public void setSunCpuEndian(String sunCpuEndian) {
        this.sunCpuEndian = sunCpuEndian;
    }

    /**
     * getSunCpuIsalist方法。
     * @return String类型返回值
     */
    public String getSunCpuIsalist() {
        return sunCpuIsalist;
    }

    /**
     * setSunCpuIsalist方法。
     *      * @param sunCpuIsalist String类型参数
     */
    public void setSunCpuIsalist(String sunCpuIsalist) {
        this.sunCpuIsalist = sunCpuIsalist;
    }

    /**
     * getSunIoUnicodeEncoding方法。
     * @return String类型返回值
     */
    public String getSunIoUnicodeEncoding() {
        return sunIoUnicodeEncoding;
    }

    /**
     * setSunIoUnicodeEncoding方法。
     *      * @param sunIoUnicodeEncoding String类型参数
     */
    public void setSunIoUnicodeEncoding(String sunIoUnicodeEncoding) {
        this.sunIoUnicodeEncoding = sunIoUnicodeEncoding;
    }

    /**
     * getSunJavaCommand方法。
     * @return String类型返回值
     */
    public String getSunJavaCommand() {
        return sunJavaCommand;
    }

    /**
     * setSunJavaCommand方法。
     *      * @param sunJavaCommand String类型参数
     */
    public void setSunJavaCommand(String sunJavaCommand) {
        this.sunJavaCommand = sunJavaCommand;
    }

    /**
     * getSunJavaLauncher方法。
     * @return String类型返回值
     */
    public String getSunJavaLauncher() {
        return sunJavaLauncher;
    }

    /**
     * setSunJavaLauncher方法。
     *      * @param sunJavaLauncher String类型参数
     */
    public void setSunJavaLauncher(String sunJavaLauncher) {
        this.sunJavaLauncher = sunJavaLauncher;
    }

    /**
     * getSunJnuEncoding方法。
     * @return String类型返回值
     */
    public String getSunJnuEncoding() {
        return sunJnuEncoding;
    }

    /**
     * setSunJnuEncoding方法。
     *      * @param sunJnuEncoding String类型参数
     */
    public void setSunJnuEncoding(String sunJnuEncoding) {
        this.sunJnuEncoding = sunJnuEncoding;
    }

    /**
     * getSunManagementCompiler方法。
     * @return String类型返回值
     */
    public String getSunManagementCompiler() {
        return sunManagementCompiler;
    }

    /**
     * setSunManagementCompiler方法。
     *      * @param sunManagementCompiler String类型参数
     */
    public void setSunManagementCompiler(String sunManagementCompiler) {
        this.sunManagementCompiler = sunManagementCompiler;
    }

    /**
     * getSunOsPatchLevel方法。
     * @return String类型返回值
     */
    public String getSunOsPatchLevel() {
        return sunOsPatchLevel;
    }

    /**
     * setSunOsPatchLevel方法。
     *      * @param sunOsPatchLevel String类型参数
     */
    public void setSunOsPatchLevel(String sunOsPatchLevel) {
        this.sunOsPatchLevel = sunOsPatchLevel;
    }

    /**
     * getUserCountry方法。
     * @return String类型返回值
     */
    public String getUserCountry() {
        return userCountry;
    }

    /**
     * setUserCountry方法。
     *      * @param userCountry String类型参数
     */
    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    /**
     * getUserCountryFormat方法。
     * @return String类型返回值
     */
    public String getUserCountryFormat() {
        return userCountryFormat;
    }

    /**
     * setUserCountryFormat方法。
     *      * @param userCountryFormat String类型参数
     */
    public void setUserCountryFormat(String userCountryFormat) {
        this.userCountryFormat = userCountryFormat;
    }

    /**
     * getUserDir方法。
     * @return String类型返回值
     */
    public String getUserDir() {
        return userDir;
    }

    /**
     * setUserDir方法。
     *      * @param userDir String类型参数
     */
    public void setUserDir(String userDir) {
        this.userDir = userDir;
    }

    /**
     * getUserHome方法。
     * @return String类型返回值
     */
    public String getUserHome() {
        return userHome;
    }

    /**
     * setUserHome方法。
     *      * @param userHome String类型参数
     */
    public void setUserHome(String userHome) {
        this.userHome = userHome;
    }

    /**
     * getUserLanguage方法。
     * @return String类型返回值
     */
    public String getUserLanguage() {
        return userLanguage;
    }

    /**
     * setUserLanguage方法。
     *      * @param userLanguage String类型参数
     */
    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    /**
     * getUserName方法。
     * @return String类型返回值
     */
    public String getUserName() {
        return userName;
    }

    /**
     * setUserName方法。
     *      * @param userName String类型参数
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * getUserTimezone方法。
     * @return String类型返回值
     */
    public String getUserTimezone() {
        return userTimezone;
    }

    /**
     * setUserTimezone方法。
     *      * @param userTimezone String类型参数
     */
    public void setUserTimezone(String userTimezone) {
        this.userTimezone = userTimezone;
    }

    /**
     * getVisualvmId方法。
     * @return String类型返回值
     */
    public String getVisualvmId() {
        return visualvmId;
    }

    /**
     * setVisualvmId方法。
     *      * @param visualvmId String类型参数
     */
    public void setVisualvmId(String visualvmId) {
        this.visualvmId = visualvmId;
    }

    /**
     * getProperties方法。
     * @return Properties类型返回值
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * setProperties方法。
     *      * @param properties Properties类型参数
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * init方法。
     */
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
    /**
     * toString方法。
     * @return String类型返回值
     */
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
    /**
     * equals方法。
     *      * @param o Object类型参数
     * @return boolean类型返回值
     */
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
    /**
     * hashCode方法。
     * @return int类型返回值
     */
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