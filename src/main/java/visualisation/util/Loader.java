package visualisation.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import visualisation.App;

/**
 * Author: Alex Nicholson
 * Date: 01/08/2021
 *
 * Utility class to handle loading FXML files
 */
public class Loader {
    /**
     * Loads the specified fxml file
     * 
     * @param fxml the path to the fxml file
     * @return a javafx node hierarchy
     */
    public static Node loadFXML(String fxml, Object controller) {
        if (fxml != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                if (controller != null) {
                    loader.setController(controller);
                }
                loader.setLocation(App.class.getResource(fxml.toString()));
                Node node = (Node) loader.load();

                return node;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Node loadFXML(String fxml) {
        return loadFXML(fxml, null);
    }

    /**
     * Used when a reference to the controller is also required
     * 
     * @param fxml the path to the fxml file
     * @return the fxmlloader which can then be used to access the fxml as well as
     *         the controller object
     */
    public static FXMLLoader manualLoad(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource(fxml.toString()));
            return loader;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}