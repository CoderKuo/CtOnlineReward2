plugins {
    id("java")
    `java-library`
}

group = "com.dakuo.demomodule"
version = "1.0.0"

dependencies {
    compileOnly("ink.ptms.core:v11902:11902-minimize:universal")
    compileOnly("ink.ptms.core:v11902:11902-minimize:mapped")
    compileOnly(project(":Plugin"))

}
