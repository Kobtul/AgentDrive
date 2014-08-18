package cz.agents.highway.vanet;

/**
 * Created by ondra on 13.8.14.
 *
 * Object to save features of object
 */
public class Status {

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public Status(String message){
        this.message = message;
    }

    public Status(){

    }

    @Override
    public String toString() {
        String statesToPrint = "";
        statesToPrint += "\n__________________________objectID " + getMessage();
        return statesToPrint;
    }
}
