package com.example.test2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by Kuba on 2015-04-18.
 */
public class TestGLSurface extends GLSurfaceView {
    private  final MyGLRenderer mRenderer;

    public TestGLSurface(Context context){
        super(context);
        mRenderer = new MyGLRenderer();
        setEGLContextClientVersion(2);



        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

    }

    public TestGLSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRenderer = new MyGLRenderer();
      //  init();
    }
//    public TestGLSurface(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        init();
//    }
    private void init(){
        // Create an OpenGL ES 2.0 context

    }
}
