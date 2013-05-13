package org.andengine.limbo.widgets;

import org.andengine.entity.text.Text;
import org.andengine.limbo.utils.positioner.PositionerSceneRelative;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.IEaseFunction;


public class SwingSpriteText extends SwingSprite<CharSequence> {
	// ===========================================================
	// Constants
	// ===========================================================

	private Text mText;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	public SwingSpriteText(final float pX, final float pY, final float pWidth, final float pHeight, final Font pFont, int pCharsCapacity,
			final ITextureRegion pTextureRegion, final float pInsetLeft, final float pInsetTop, final float pInsetRight, final float pInsetBottom,
			eAnimationDirection pAnimationOutDirection, eAnimationDirection pAnimationInDirection, final float pAnimationTimeOut, final float pAnimationTimeIn, 
			final IEaseFunction pEasingOut, final IEaseFunction pEasingIn, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pInsetLeft, pInsetTop, pInsetRight, pInsetBottom,
				pAnimationOutDirection, pAnimationInDirection, pAnimationTimeOut, pAnimationTimeIn,
				pEasingOut, pEasingIn, pVertexBufferObjectManager);
		mText = new Text(0, 0, pFont, "123456789", pCharsCapacity, pVertexBufferObjectManager);
		attachChild(mText);
		PositionerSceneRelative.getInstance().center(getContainer(), mText);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void updateValue(CharSequence pValue) {
		mText.setText(pValue);
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}