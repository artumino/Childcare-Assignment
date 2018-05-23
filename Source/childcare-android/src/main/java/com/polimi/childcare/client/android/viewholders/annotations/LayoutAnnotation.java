package com.polimi.childcare.client.android.viewholders.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LayoutAnnotation 
{
    int LayoutID();
}
