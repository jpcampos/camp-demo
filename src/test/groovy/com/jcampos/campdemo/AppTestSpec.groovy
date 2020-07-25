package com.jcampos.campdemo

import com.jcampos.campdemo.CampDemoApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles(profiles = "test")
@SpringBootTest(classes= CampDemoApplication)
class AppTestSpec extends Specification {
    def "cntx loads"() {
        expect:""
    }
}