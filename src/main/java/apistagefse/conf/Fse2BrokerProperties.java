package apistagefse.conf;


import apistagefse.base.stream.StreamOp;
import apistagefse.route.rest.basic.PlainUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author b.amoruso
 */

@Configuration
@ConfigurationProperties(prefix = "apistagefse")
@Component
@Getter
@Setter
public class Fse2BrokerProperties {

    private File cacheDirectory;
    private String gwUrlService;
    private String schematronPath;
    private String trustPassword;
    private String fileBaseSign;
    private String repositoryDir;
    private String userSignature;
    private String assigningAuthority;

    private String restUsername;

    private String restPassword;

    private List<String> restApplication;

    private String trustStoreFile;

    private File tempDirectory;

    private boolean oauthEnabled;

    private int maximumRedeliveries;

    public void setRestApplication(List<String> items) {
        this.restApplication = items;
    }

    public boolean validation;

    public List<String> getRawRestApplication() {

        List<String> _restApplication = new ArrayList<>();
        _restApplication.add("::1,test,test");

        return restApplication == null ? _restApplication : restApplication;
    }

    public Collection<PlainUser> getRestApplication() {
        return PlainUser.toListOfUser(getRawRestApplication());
    }


    public File getTempDirectory() {
        return StreamOp.makeDirectory(new File(getCacheDirectory(), "temp"));
    }
}
