package visualisation.controller;

public enum Views {

    CHART("view/Chart.fxml"),
    GRAPH("view/InputGraph.fxml"),
    STATS("view/Statistics.fxml"),
    CHART_ROW("view/ChartRow.fxml"),
    ACTION("view/ActionPanel.fxml"),
    OPTIMALTIME("view/OptimalTime.fxml")
    ;

    private final String path;

    /**
     * Private constructor
     * 
     * @param path the path to the associated fxml file
     */
    private Views(String path) {
        this.path = path;
    }

    /**
     * @return the path to the fxml file
     */
    public String getPath() {
        return path;
    }
}
