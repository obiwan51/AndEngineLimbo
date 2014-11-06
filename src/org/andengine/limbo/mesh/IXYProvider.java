package org.andengine.limbo.mesh;
public interface IXYProvider {

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
	public void calculateXY();
	public FloatChain getX();
	public FloatChain getY();
	public int getNumberOfVertices();
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
