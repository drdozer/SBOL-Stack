package ncl.ac.uk;

import org.openrdf.OpenRDFException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.rio.RDFFormat;
import java.io.File;
import org.openrdf.repository.http.HTTPRepository;

/**
 * RDF submitted tool to submit RDF files in turtle files to a Sesame server.
 * @author Goksel Misirli
 */

public class Submitter
{
    public Submitter()
    {
    }

    /**
     * Submits the given RDF file in Turtle format  to a given sesame server
     * @param server The URL of the sesame server E.g: "http://localhost:8080/openrdf-sesame
     * @param repositoryID :The ID of the repository
     * @param rdfFilePath  : File path to the RDF data
     * @param baseURI      : Base URI in the RDF file
     * @throws Exception
     */
    public void Submit(String server, String repositoryID, String rdfFilePath,String baseURI) throws Exception
    {

        Repository myRepository = new HTTPRepository(server, repositoryID);
        myRepository.initialize();

        File file = new File(rdfFilePath);


        try
        {
            RepositoryConnection con = myRepository.getConnection();
            try
            {
                System.out.print ("Starting to add the data to " + server + "...");
                RDFFormat format = RDFFormat.TURTLE;
                RDFFormat turtle = new RDFFormat(format.getName(), "application/x-turtle", format.getCharset(), "ttl", true, true);
                con.add(file, baseURI, turtle);
                System.out.println ("done!");
            }
            finally
            {
                con.close();
            }
        }
        catch (OpenRDFException e)
        {
            throw new Exception("Could not upload the data", e);
        }
        catch (java.io.IOException e)
        {
            throw new Exception("Could not read the file", e);
        }
    }

    public static void main(String[] args) throws Exception
    {
        if (args==null || args.length!=4)
        {
            System.out.println ("Usage:<Server> <RepositoryID> <rdfFilePath> <baseURI>");
            return;
        }
        
        String server=args[0];
        String repositoryID=args[1];
        String filePath=args[2];
        String baseURI=args[3];
        Submitter submitter = new Submitter();
        submitter.Submit(server, repositoryID,filePath,baseURI);
    }
}
