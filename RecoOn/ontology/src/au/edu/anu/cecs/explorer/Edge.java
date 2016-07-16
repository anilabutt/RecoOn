package au.edu.anu.cecs.explorer;

import java.io.Serializable;

public class Edge implements Serializable
{
    public final Vertex target;
    public final double weight;
    public Edge(Vertex argTarget, double argWeight)
    { target = argTarget; weight = argWeight; }
}