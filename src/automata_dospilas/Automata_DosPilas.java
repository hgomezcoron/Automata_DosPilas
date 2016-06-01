import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Stack;
import java.util.Vector;

public class Automata_DosPilas {

    public static void main(String args[]) {

        String file_transitions = "";
        String w = "";

        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        try {
            file_transitions = console.readLine();
            w = console.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        //Create Turing Machine
        Automata_DosPilas automataDosPilas = new Automata_DosPilas();
        //Create file access handler
        File f = new File(file_transitions);
        try {
            FileReader fr = new FileReader(f);//Create file reader
            BufferedReader lines = new BufferedReader(fr);//Read from file
            String line = lines.readLine().toString();
            //While exists line with transitions
            while (line != null) {
                automataDosPilas.addTransition(line);//Add transition to Turing Machine
                line = lines.readLine();//Read next line of the file
            }
            fr.close();
            //Evaluate the input with Turing Machine
            automataDosPilas.process(w);//Invoke a process in the machine
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Dynamic vector saves all transitions of Turing Machine
    private Vector<String[]> transitions = null;

    public Automata_DosPilas() {
        //Instances the Transitions Vector
        this.transitions = new Vector<String[]>();
    }

    public void addTransition(String newTransition) {
        String[] transition = newTransition.split(","); //Separate all fields by the colon
        for (int i = 0; i < transition.length; i++) {
            if (transition[i].equals("lambda")) {
                transition[i] = "-";
            }
        }
        this.transitions.add(transition); //Add the new transition to Transitions Vector
    }

    public void process(String input) {

        // El estado actual siempre sera q0
        String estadoActual = "q0";

        // El estado aceptación siempre sera qf
        String estadoAceptacion = "qf";

        // Se crea la pila
        Stack<String> pila1 = new Stack<String>();
        Stack<String> pila2 = new Stack<String>();

        // Se inicializa la pila con Z0
        pila1.push("z0");
        pila2.push("z0");

        boolean aceptada = false;

        String log = "";

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        String cadena = input + "-";
        boolean proceso = true;
        for (int i = 0; i < cadena.length(); i++) {
            if (proceso) {
                proceso = false;
                String tope1 = pila1.pop();
                String tope2 = pila2.pop();
                pila1.push(tope1);
                pila2.push(tope2);
                String simboloActual = cadena.charAt(i) + "";
                for (int j = 0; j < transitions.size(); j++) {
                    if (estadoActual.equals(transitions.get(j)[0])) {
                        if (simboloActual.equals(transitions.get(j)[1])) {
                            if (tope1.equals(transitions.get(j)[2])) {
                                if (tope2.equals(transitions.get(j)[3])) {
                                    estadoActual = transitions.get(j)[4];
                                    if (transitions.get(j)[5].equals("-")) {
                                        pila1.pop();
                                    } else if (transitions.get(j)[5].length() > 1 && (transitions.get(j)[5].toString().charAt(1)) == ('z')) {
                                        pila1.pop();
                                        pila1.push(transitions.get(j)[2]);
                                        pila1.push(transitions.get(j)[5].toString().charAt(0) + "");
                                    } else if (transitions.get(j)[5].length() > 1 && (transitions.get(j)[5].toString().charAt(0)) != ('z')) {
                                        pila1.pop();
                                        pila1.push(transitions.get(j)[2]);
                                        pila1.push(transitions.get(j)[5].toString().charAt(0) + "");
                                    } else if (transitions.get(j)[5].length() == 1) {
                                        pila1.pop();
                                        pila1.push(transitions.get(j)[5].toString().charAt(0) + "");
                                    }
                                    if (transitions.get(j)[6].equals("-")) {
                                        pila2.pop();
                                    } else if (transitions.get(j)[6].length() > 1 && (transitions.get(j)[6].toString().charAt(1)) == ('z')) {
                                        pila2.pop();
                                        pila2.push(transitions.get(j)[3]);
                                        pila2.push(transitions.get(j)[6].toString().charAt(0) + "");
                                    } else if (transitions.get(j)[6].length() > 1 && (transitions.get(j)[6].toString().charAt(0)) != ('z')) {
                                        pila2.pop();
                                        pila2.push(transitions.get(j)[3]);
                                        pila2.push(transitions.get(j)[6].toString().charAt(0) + "");
                                    } else if (transitions.get(j)[6].length() == 1) {
                                        pila2.pop();
                                        pila2.push(transitions.get(j)[6].toString().charAt(0) + "");
                                    }
                                    proceso = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (j >= transitions.size()) {
                        aceptada = false;
                        break;
                    }
                }
            }
        }
        if (pila1.pop().equals("z0") && pila2.pop().equals("z0") && estadoAceptacion.contains(estadoActual)) {
            aceptada = true;
        }
        try {
            File file = new File("Resultados.txt");
            if (!file.exists()) {
                // creates the file
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter fileWriter = new BufferedWriter(fw);

            bw.write(log); //Add to print a log of Turing Machine movements

            //Evaluate the input with Turing Machine
            if (aceptada) //Invoke a process in the machine
            {
                bw.write("\nComputo terminado"); //Input leave at machine to finish in a FinalState
                fileWriter.write("\nComputo terminado");
            } else {
                bw.write("\nComputo rechazado"); //Input don't leave at machine to finish in a FinalState
                fileWriter.write("\nComputo rechazado");
            }
            bw.flush(); //Print in the console
            fileWriter.newLine();
            fileWriter.flush();
            bw.close(); //Close buffer
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}