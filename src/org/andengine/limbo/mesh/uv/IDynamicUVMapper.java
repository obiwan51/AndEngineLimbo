package org.andengine.limbo.mesh.uv;

import org.andengine.engine.handler.IUpdateHandler;

public interface IDynamicUVMapper extends IUVMapper,IUpdateHandler {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	boolean isDirty();
	void setDirty(boolean pDirty);
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
