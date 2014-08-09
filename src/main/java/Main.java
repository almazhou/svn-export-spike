import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.File;
import java.util.Properties;

public class Main {
    public static void main(String ... args){
        PropertyReader propertyReader = new PropertyReader();
        Properties properties = propertyReader.getProperties("dev_svn_config.properties");
        final String url = properties.getProperty("svn_url");
        final String destPath = "src/main/resources";

        SVNRepository repository = null;
        try{
            //initiate the reporitory from the url
            repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(url));
            //create authentication data
            String userName = properties.getProperty("user_name");
            String password = properties.getProperty("password");
            ISVNAuthenticationManager authManager =
                    SVNWCUtil.createDefaultAuthenticationManager(userName, password);
            repository.setAuthenticationManager(authManager);
            //output some data to verify connection
            System.out.println( "Repository Root: " + repository.getRepositoryRoot( true ) );
            System.out.println(  "Repository UUID: " + repository.getRepositoryUUID( true ) );
            //need to identify latest revision
            long latestRevision = repository.getLatestRevision();
            System.out.println(  "Repository Latest Revision: " + latestRevision);

            //create client manager and set authentication
            SVNClientManager ourClientManager = SVNClientManager.newInstance();
            ourClientManager.setAuthenticationManager(authManager);
            //use SVNUpdateClient to do the export
            SVNUpdateClient updateClient = ourClientManager.getUpdateClient( );
            updateClient.setIgnoreExternals( false );
            updateClient.doExport( repository.getLocation(), new File(destPath),
                    SVNRevision.create(latestRevision), SVNRevision.create(latestRevision),
                    null, true, SVNDepth.INFINITY);

        } catch (SVNException e) {
            e.printStackTrace();
        }finally {
            System.out.println("Done");
        }
    }

}
