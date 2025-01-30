dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs{
        create("libs"){
            // 작성한 버전 카탈로그 toml 파일을 가져와준다
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")