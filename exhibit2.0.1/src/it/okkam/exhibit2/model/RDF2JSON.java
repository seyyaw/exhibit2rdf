package it.okkam.exhibit2.model;

import java.io.InputStream;
import org.json.JSONException;
import org.json.JSONObject;
import org.openjena.atlas.logging.Log;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
public class RDF2JSON {
	private final String inputDatasetFileName = "resources/test/mockup.ttl";
	private Model inputModel = null;
	private static String baseUri = null;
	public static void main(String argv[]) {
		RDF2JSON rdf2json = new RDF2JSON();
		rdf2json.loadInputModel();
		StmtIterator stmtit = rdf2json.inputModel.listStatements();
		ResIterator resourceit=rdf2json.inputModel.listResourcesWithProperty(null);
		System.out.println("\"{\"items\":\n\t[");
		while (resourceit.hasNext()) {
		Resource resource = resourceit.nextResource();
			JSONSerializer serializer = new JSONSerializer(false);
			serializer.putNamespace(
					"http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf");
			serializer.putNamespace(
					"http://models.okkam.org/ENS-core-vocabulary.owl#", "ens");
			try {
				JSONObject jo = (JSONObject) serializer.objectify(resource);
				System.out.println(jo.toString()+",");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		System.out.println("{}\n\t]\n}");
	}
	private void loadInputModel() {
		InputStream in = FileManager.get().open(inputDatasetFileName);
		if (in == null) {
			Log.info(RDF2JSON.class, "File: " + inputDatasetFileName
					+ " not found");
			System.exit(0);
		}
		inputModel = ModelFactory.createDefaultModel();
		inputModel.read(in, baseUri, "TURTLE");
	}
}
