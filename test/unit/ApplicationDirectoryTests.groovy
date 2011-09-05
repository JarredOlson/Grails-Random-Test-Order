import grails.test.GrailsUnitTestCase
import org.junit.Test
import com.randomtestorder.ApplicationDirectory

@Mixin(ApplicationDirectory)
class ApplicationDirectoryTests extends GrailsUnitTestCase {

    @Test
    void filpBackSlashesToForwardSlashesShouldReturnNullWhenGivenNull() {
        def result = filpBackSlashesToForwardSlashes(null)

        assert null == result
    }

    @Test
    void filpBackSlashesToForwardSlashesShouldReturnEmptyStringWhenGivenEmptyString() {
        def result = filpBackSlashesToForwardSlashes("")

        assert "" == result
    }

    @Test
    void filpBackSlashesToForwardSlashesShouldReturnReplaceAllBackSlashesWithForwardSlashes() {
        def result = filpBackSlashesToForwardSlashes("\\One\\Two\\Three/")

        assert "/One/Two/Three/" == result
    }

}
