package org.pavlos.restapispec.client.generator;

import freemarker.template.Configuration;
import org.pavlos.restapispec.interfaces.APISpec;

import java.io.File;

abstract class CodeGenerator {

    protected final APISpec apiSpec;

    protected final Configuration cfg;

    protected CodeGenerator(APISpec apiSpec, Configuration cfg) {
        this.apiSpec = apiSpec;
        this.cfg = cfg;
    }

    protected abstract void validateForTargetLanguage(APISpec apiSpec);

    protected abstract void generateClientCode(File dest, String clientPackage, String clientName);

    protected abstract void generateTestClientCode(File dest, String clientPackage, String clientName, String mockPackage, String mockName);

    protected abstract void generateTestServerCode(File dest, String clientPackage, String clientName, String testPackage, String testName, String serverPort);

    public void generateClient(File clientDest, File testClientDest, String clientPackage, String clientName, String mockPackage, String mockName) {
        validateForTargetLanguage(apiSpec);
        generateClientCode(clientDest, clientPackage, clientName);
        generateTestClientCode(testClientDest, clientPackage, clientName, mockPackage, mockName);
    }

    public void generateTests(File mockServerDest, String clientPackage, String clientName, String testPackage, String testName, String serverPort) {
        generateTestServerCode(mockServerDest, clientPackage, clientName, testPackage, testName, serverPort);
    }
}