package com.zifang.util.devops.nexus;


public class Checksum {

    private String sha1;

    private String md5;

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Override
    public String toString() {
        return "Checksum{sha1=" + sha1 + ", md5=" + md5 + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Checksum checksum = (Checksum) o;
        return java.util.Objects.equals(sha1, checksum.sha1) && java.util.Objects.equals(md5, checksum.md5);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(sha1, md5);
    }
}
