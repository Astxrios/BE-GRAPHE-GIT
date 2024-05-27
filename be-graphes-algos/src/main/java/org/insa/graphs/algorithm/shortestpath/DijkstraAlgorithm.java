package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.algorithm.AbstractInputData.Mode;



public class DijkstraAlgorithm extends ShortestPathAlgorithm {


    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {

        ArrayList<Label> LabelArray = new ArrayList<Label>();
        final ShortestPathData data = getInputData();
        Mode m = data.getMode();
        double vmax = data.getGraph().getGraphInformation().getMaximumSpeed();
        
        //On initialise les labels (+oo)
        for (Node node : data.getGraph().getNodes()) {  // Initialisation du tableau
            double dist_o = node.getPoint().distanceTo(data.getDestination().getPoint());
            LabelArray.add(LabelInit(node,false,Double.MAX_VALUE,null,null,dist_o,vmax,m));
        }

        //On initialise le  label d'origine que l'on met dans le tas
        Label originLabel = LabelArray.get(data.getOrigin().getId());
        originLabel.setCost(0);  // Coût de l'origine mis à 0
        notifyOriginProcessed(data.getOrigin());

        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        heap.insert(originLabel);

    
        boolean clef = true;
        while(! heap.isEmpty() && clef) {
            // tant que le tas n'est pas vide on extrait le label correspondant au sommet le plus proche de celui d'origine
            Label currentLabel = heap.deleteMin();
            currentLabel = LabelArray.get(LabelArray.indexOf(currentLabel));
            currentLabel.setMarque(true);
            notifyNodeMarked(currentLabel.getSommet_courant());

            // avant de continuer on s'assure que l'on a pas deja atteint l'objectif
            if(currentLabel==LabelArray.get(data.getDestination().getId())){
                clef = false;
            }
            if(clef){
                //si ce n'est pas le cas alors on explore les voisins à l'aide des arcs du sommet depile
                for (Arc arc : currentLabel.getSommet_courant().getSuccessors()) {
                    
                    //on s'assure qu'on a  le droit d'utiliser l'arc
                    if (!data.isAllowed(arc)) {
                        continue;
                    }

                    Label successorLabel = LabelArray.get(arc.getDestination().getId());
                    double cout = data.getCost(arc);

                    //si c'est le cas on met a jour la distance et on place le voisin dans le tas s'il apparait pour la premiere fois
                    if((successorLabel.getCout_realise()>(currentLabel.getCout_realise()+cout) && !successorLabel.isMarque())){
                        if(successorLabel.getCout_realise()!=Double.MAX_VALUE){
                            heap.remove(successorLabel);
                        } else {
                            notifyNodeReached(arc.getDestination());
                        }
                        successorLabel.setCost(currentLabel.getCout_realise()+cout);
                        successorLabel.setPere(arc);
                        successorLabel.setsommetpere(currentLabel.getSommet_courant());
                        heap.insert(successorLabel);
                    }
                }
            }
        }

        //Une fois tout les noeuds explores ou l'arrivee atteinte on reconstruit le chemin de la meme maniere que l'algorithme de Bellman
        Label destinationLabel = LabelArray.get(data.getDestination().getId());
        notifyDestinationReached(destinationLabel.getSommet_courant());

        //System.out.println("On a trouvé un chemin, sauf problemes");
        ShortestPathSolution solution = null;
        if (! destinationLabel.isMarque()) {
            solution = new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
        } else {
            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = LabelArray.get(data.getDestination().getId()).getPere();
            while (arc!=null) {
                arcs.add(arc);
                arc=LabelArray.get(arc.getOrigin().getId()).getPere();
            }

            Collections.reverse(arcs);
            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(data.getGraph(), arcs));
        }

        return solution;
    }

    protected Label LabelInit(Node sc, boolean m, double cr,Arc p,Node sp,double dist_o,double vmax, Mode mo){
        return new Label(sc,m,cr,p,sp);
    }
    

}
