package org.andengine.limbo.widgets.swing;

import java.util.Stack;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2013 Michal Stawinski (nazgee)
 *
 * @author Michal Stawinski
 * @since 20:31:01 - 13.05.2013
 */
public class SwingSpriteTextStacked extends SwingSpriteText {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	protected Stack<CharSequence> mStack = new Stack<CharSequence>();
	// ===========================================================
	// Constructors
	// ===========================================================
	public SwingSpriteTextStacked(final float pX, final float pY, final float pWidth, final float pHeight, final Font pFont, int pCharsCapacity,
			final ITextureRegion pTextureRegion, final float pInsetLeft, final float pInsetTop, final float pInsetRight, final float pInsetBottom,
			eAnimationDirection pAnimationOutDirection, eAnimationDirection pAnimationInDirection, final float pAnimationTimeOut, final float pAnimationTimeIn, 
			final IEaseFunction pEasingOut, final IEaseFunction pEasingIn, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pFont, pCharsCapacity,
				pTextureRegion, pInsetLeft, pInsetTop, pInsetRight, pInsetBottom,
				pAnimationOutDirection, pAnimationInDirection, pAnimationTimeOut, pAnimationTimeIn,
				pEasingOut, pEasingIn, pVertexBufferObjectManager);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public void pushValueBlockDuplicates(CharSequence pValue) {
		pushValue(pValue, true);
	}

	public void pushValueAllowDuplicates(CharSequence pValue) {
		pushValue(pValue, false);
	}

	public void pushValue(CharSequence pValue, boolean blockDuplicates) {
		if (blockDuplicates && getValue() != null && getValue().equals(pValue)) {
			return;
		} else {
			super.setValue(pValue);
			this.mStack.push(pValue);
		}
	}

	public void popValue() {
		if (this.mStack.size() > 0) {
			this.mStack.pop();
			if (this.mStack.size() > 0) {
				super.setValue(this.mStack.peek());
			}
		}
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
