package allure_steps_api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import static constants_api.EndPointsURL.MAIN_URL;

public class SpecificationSetUp {
    protected RequestSpecification getSpec(){
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(MAIN_URL)
                .build();
    }
}
