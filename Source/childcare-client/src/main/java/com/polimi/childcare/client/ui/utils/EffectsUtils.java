package com.polimi.childcare.client.ui.utils;

import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;

public class EffectsUtils
{
    public static void AddGlow(Node node, double intensity)
    {
        if(!(node.getEffect() instanceof Glow))
        {
            Glow glow = new Glow();
            glow.setLevel(intensity);
            glow.setInput(node.getEffect());
            node.setEffect(glow);
        }
    }

    public static void RemoveGlow(Node node)
    {
        if(node.getEffect() instanceof Glow)
        {
            Effect effect = ((Glow)node.getEffect()).getInput();
            node.setEffect(effect);
        }
    }
}
