package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.algorithm.AbstractInputData.Mode;


public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    protected Label LabelInit(Node sc, boolean m, double cr,Arc p,Node sp,double dist_o,double vmax,Mode mo){
        if(mo==Mode.TIME){
            dist_o = dist_o/vmax;
        } 
        return new LabelStar(sc,m,cr,p,sp,dist_o);
    }

}
