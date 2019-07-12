package RietFlipper.Util;

import org.dreambot.api.methods.MethodProvider;

import RietFlipper.RietFlipper;

public abstract class Node {
    protected final RietFlipper c;

    public Node(RietFlipper c) {
    	this.c = c;
    }
    public abstract boolean validate();
    public abstract int execute();

}
