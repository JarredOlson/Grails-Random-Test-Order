package com.randomtestorder


class ApplicationDirectory {

    def getApplicationRootDirectory() {
        filpBackSlashesToForwardSlashes(System.properties['base.dir'])
    }

    def filpBackSlashesToForwardSlashes(String str) {
        if(str == null) {return null}
        str.replaceAll('\\\\', '/')
    }

    def getApplicationUnitTestDirectory() {
        getApplicationRootDirectory() + "/test/unit"
    }

    def getApplicationIntegrationTestDirectory() {
        getApplicationRootDirectory() + "/test/integration"
    }
}
