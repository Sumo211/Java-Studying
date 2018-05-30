package com.leon.structure;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GraphNode {

    private String label;

    private Set<GraphNode> neighbours;

    private Optional<String> color;

    public GraphNode(String label) {
        this.label = label;
        neighbours = new HashSet<>();
        color = Optional.empty();
    }

    public String getLabel() {
        return label;
    }

    public boolean hasColor() {
        return color.isPresent();
    }

    public String getColor() {
        return color.get();
    }

    public void setColor(String color) {
        this.color = Optional.ofNullable(color);
    }

    public Set<GraphNode> getNeighbours() {
        return Collections.unmodifiableSet(neighbours);
    }

    public void setNeighbours(GraphNode neighbour) {
        neighbours.add(neighbour);
    }

}
