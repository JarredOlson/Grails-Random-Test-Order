import org.junit.Test
import com.randomtestorder.ApplicationDirectory

@Mixin(ApplicationDirectory)
class ApplicationDirectoryIntTests extends GroovyTestCase {

    @Test
    void getApplicationRootDirectoryReturnsRootDirectoryWithForwardSlashes() {
        assert filpBackSlashesToForwardSlashes(System.properties['base.dir']) == getApplicationRootDirectory()
    }

    @Test
    void getApplicationUnitTestDirectoryReturnsCorrectDirectory() {
        def result = getApplicationUnitTestDirectory()
        assert getApplicationRootDirectory() + "/test/unit" == result
    }

    @Test
    void getApplicationIntegrationTestDirectoryReturnsCorrectDirectory() {
        def result = getApplicationIntegrationTestDirectory()
        assert getApplicationRootDirectory() + "/test/integration" == result
    }
}
