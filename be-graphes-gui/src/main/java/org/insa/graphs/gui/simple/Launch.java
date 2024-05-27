package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import java.util.Random;
import org.insa.graphs.model.Path;





public class Launch {

    private static List<ArcInspector> listeinspector;
    private static ArcInspector arcInspector;
    private static ShortestPathData data;
    private static BellmanFordAlgorithm BFA;
    private static DijkstraAlgorithm DA;
    private static AStarAlgorithm AA;
    private static ShortestPathSolution sBFA;
    private static ShortestPathSolution sDA;
    private static ShortestPathSolution sAA;
    //private static Graph gWashington = null; trop grand pour Bellman
    //private static Graph gBelgique = null; trop grand pour Bellman
    //private static Graph gDense = null;
    private static Graph gCarre = null;
    private static Graph gINSA = null;
    private static Graph gToulouse = null;

    private static GraphReader readerINSA;
    //private static GraphReader readerBelgique;
    //private static GraphReader readerWashington;
    //private static GraphReader readerDense;
    private static GraphReader readerCarre;
    private static GraphReader readerToulouse;

    /**
     * Create a new Drawing inside a JFrame an return it.
     * 
     * @return The created drawing.
     * 
     * @throws Exception if something wrong happens when creating the graph.
     */
    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }

    /*public static void main(String[] args) throws Exception {
        final String mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
        final String pathName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";
        final GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        final Graph graph = reader.read();
        final Drawing drawing = createDrawing();
        drawing.drawGraph(graph);
        final PathReader pathReader = new BinaryPathReader(new DataInputStream(new BufferedInputStream(new FileInputStream(pathName))));
        final Path path = pathReader.readPath(graph);
        drawing.drawPath(path);
    }*/

    public static void initAll() throws Exception {
        String INSA = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
        //String Belgique = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/belgium.mapgr";
        //String Washington = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/washington.mapgr";
        //String Dense = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre-dense.mapgr";
        String Carre = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre.mapgr";
        String Toulouse = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr";

        readerINSA = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(INSA)))
        );
        /*readerBelgique= new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(Belgique)))
        );
        readerWashington = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(Washington)))
        );
        readerDense = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(Dense)))
        );*/
        readerCarre = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(Carre)))
        );
        readerToulouse = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(Toulouse)))
        );

    }

    public static void initialize(int O, int D, int route, Graph g){
        
        Node Origine = g.get(O);
        Node Arrivee = g.get(D);
        listeinspector=ArcInspectorFactory.getAllFilters();
        arcInspector = listeinspector.get(route);
        data = new ShortestPathData(g, Origine, Arrivee, arcInspector);

        DA = new DijkstraAlgorithm(data);
        BFA = new BellmanFordAlgorithm(data);
        AA = new AStarAlgorithm(data);

        sDA = DA.run();
        sAA = AA.run();
        sBFA = BFA.run();


    }

    public static void main(String[] args) throws Exception{
        initAll();

        //INSA
        System.out.println("Map INSA");
        Random rand = new Random();
        System.out.println("INSA :");
        final Graph gINSA = readerINSA.read();
        for(int i=0;i<100;i++){
            int point1 = rand.nextInt(1348);
            int point2 = rand.nextInt(1348);
            while(point2==point1){
                point2 = rand.nextInt(1348); 
            } 
            int mode = rand.nextInt(4);
            initialize(point1, point2,mode,gINSA);
            Path p1 = sBFA.getPath();
            Path p2 = sDA.getPath();
            Path p3 = sAA.getPath();
            if(p1!=null){
                float sol = p1.getLength();
                float trouve1 = p2.getLength();
                float trouve2 = p3.getLength();
                if(sol==trouve1&&sol==trouve2){
                    System.out.println("Correct pour les points "+point1+" et "+point2);
                } else{
                    System.out.println("Problème pour les points "+point1+" et "+point2);
                } 
            } else {
                if(p2==null&&p3==null){
                    System.out.println("Correct pour les points "+point1+" et "+point2+" (Cas nul)");
                } else{
                    System.out.println("Problème pour les points "+point1+" et "+point2);
                } 
            }   
        } 
        
        
        //carre
        System.out.println("");
        System.out.println("Map Carre");
        final Graph gCarre = readerCarre.read();
        for(int i=0;i<50;i++){
            int point1 = rand.nextInt(24);
            int point2 = rand.nextInt(24);
            while(point2==point1){
                point2 = rand.nextInt(24); 
            } 
            int mode = rand.nextInt(4);
            initialize(point1, point2,mode,gCarre);
            float sol = sBFA.getPath().getLength();
            float trouve1 = sDA.getPath().getLength();
            float trouve2 = sAA.getPath().getLength();
            if(sol==trouve1&&sol==trouve2){
                System.out.println("Correct pour les points "+point1+" et "+point2);
            } else{
                System.out.println("Problème pour les points "+point1+" et "+point2);
            } 
        }
       
        //Toulouse
        System.out.println("");
        System.out.println("Map Toulouse");
        final Graph gToulouse = readerToulouse.read();
        for(int i=0;i<20;i++){
            int point1 = rand.nextInt(39857);
            int point2 = rand.nextInt(39857);
            while(point2==point1){
                point2 = rand.nextInt(39857); 
            } 
            int mode = rand.nextInt(4);
            initialize(point1, point2,mode,gToulouse);
            Path p1 = sBFA.getPath();
            Path p2 = sDA.getPath();
            Path p3 = sAA.getPath();
            if(p1!=null){
                float sol = p1.getLength();
                float trouve1 = p2.getLength();
                float trouve2 = p3.getLength();
                if(sol==trouve1&&sol==trouve2){
                    System.out.println("Correct pour les points "+point1+" et "+point2);
                } else{
                    System.out.println("Problème pour les points "+point1+" et "+point2);
                } 
            } else {
                if(p2==null&&p3==null){
                    System.out.println("Correct pour les points "+point1+" et "+point2+" (Cas nul)");
                } else{
                    System.out.println("Problème pour les points "+point1+" et "+point2);
                } 
            }   
        } 
    }

}
