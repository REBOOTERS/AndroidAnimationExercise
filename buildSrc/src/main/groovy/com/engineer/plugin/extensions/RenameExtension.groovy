package com.engineer.plugin.extensions

class RenameExtension {
    Closure result = null

    String prefix = "animation"
    Boolean withTime = false
    Boolean withVariantName = false
    Boolean withVariantVersion = false
    Boolean onlyDebug = false


    @Override
    public String toString() {
        return "RenameExtension{" +
                "result=" + result +
                ", prefix='" + prefix + '\'' +
                ", withTime=" + withTime +
                ", withVariantName=" + withVariantName +
                ", withVariantVersion=" + withVariantVersion +
                ", onlyDebug=" + onlyDebug +
                '}'
    }
}