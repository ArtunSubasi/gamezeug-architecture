import com.structurizr.model.DeploymentNode;
import com.structurizr.model.Model;

class DeploymentNodes {

    DeploymentNodes(Model model, Containers gamezeugContainers) {

        DeploymentNode aws = model.addDeploymentNode("Amazon Web Services", "", "Some region..."); // TODO what region?

        DeploymentNode bffE2c = aws.addDeploymentNode("Amazon E2C BFF", "Backend for frontends", "E2C instance"); // TODO which instance?
        bffE2c.addDeploymentNode("Container 1", "", "Docker").add(gamezeugContainers.reverseProxy);

        DeploymentNode s3 = aws.addDeploymentNode("Amazon S3", "", "S3"); // TODO CloudFront?
        s3.add(gamezeugContainers.portal);
        s3.add(gamezeugContainers.adminFrontend);
        s3.add(gamezeugContainers.tablesFrontend);
        s3.add(gamezeugContainers.chatFrontend);

        DeploymentNode backendE2c = aws.addDeploymentNode("Amazon E2C Backend", "Backend instances", "E2C instance"); // TODO which instance?
        backendE2c.addDeploymentNode("Container 2", "", "Docker").add(gamezeugContainers.authServer);
        backendE2c.addDeploymentNode("Container 3", "", "Docker").add(gamezeugContainers.adminBackend);
        backendE2c.addDeploymentNode("Container 4", "", "Docker").add(gamezeugContainers.tablesBackend);
        backendE2c.addDeploymentNode("Container 5", "", "Docker").add(gamezeugContainers.chatBackend);
        backendE2c.addDeploymentNode("Container 6", "", "Docker").add(gamezeugContainers.messageBus);

        aws.addDeploymentNode("Amazon DynamoDB", "", "DynamoDB").add(gamezeugContainers.adminDatabase);
        aws.addDeploymentNode("Amazon RDS 1", "", "PostgreSQL").add(gamezeugContainers.userDatabase);
        aws.addDeploymentNode("Amazon RDS 2", "", "MySQL").add(gamezeugContainers.tablesDatabase);
        aws.addDeploymentNode("Amazon ElastiCache for Redis", "", "Redis").add(gamezeugContainers.chatDatabase);
    }

}