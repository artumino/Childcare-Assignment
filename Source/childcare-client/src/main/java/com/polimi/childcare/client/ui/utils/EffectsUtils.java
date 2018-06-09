package com.polimi.childcare.client.ui.utils;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;

public class EffectsUtils
{
    //Glow
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

    //Shadow
    public static void AddShadow(Node node, double radius)
    {
        if(!(node.getEffect() instanceof DropShadow))
        {
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(radius);
            dropShadow.setInput(node.getEffect());
            node.setEffect(dropShadow);
        }
    }

    public static void RemoveShadow(Node node)
    {
        if(node.getEffect() instanceof DropShadow)
        {
            Effect effect = ((DropShadow)node.getEffect()).getInput();
            node.setEffect(effect);
        }
    }
}
