package org.andengine.limbo.widgets;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.limbo.utils.EntityAnimator;
import org.andengine.limbo.utils.EntityAnimator.IAnimatorListener;
import org.andengine.limbo.utils.positioner.PositionerImmovableRelative;
import org.andengine.limbo.utils.positioner.PositionerSceneRelative;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2013 Michal Stawinski (nazgee)
 *
 * @author Michal Stawinski
 * @since 20:31:01 - 13.05.2013
 */
public abstract class Swing<T> extends ClippingWindowContainer {
	// ===========================================================
	// Constants
	// ===========================================================
	protected final EntityAnimator mContainerAnimator;
	// ===========================================================
	// Fields
	// ===========================================================
	protected T mValue;
	protected T mPendingValue;
	private AnimationOutListener mAnimateOutListener = new AnimationOutListener(1);
	private final eAnimationDirection mAnimationOutDirection;
	private final eAnimationDirection mAnimationInDirection;
	private final float mAnimatioTimeIn;
	private final float mAnimatioTimeOut;
	private final IEaseFunction mEasingIn;
	private final IEaseFunction mEasingOut;
	// ===========================================================
	// Constructors
	// ===========================================================
	public Swing(final float pX, final float pY, final float pWidth, final float pHeight, final float pInsetLeft, final float pInsetTop, final float pInsetRight, final float pInsetBottom,
			eAnimationDirection pAnimationOutDirection, eAnimationDirection pAnimationInDirection, float pAnimationTimeOut, final float pAnimationTimeIn,
			final IEaseFunction pEasingOut, final IEaseFunction pEasingIn, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pWidth, pHeight);
		setPosition(pX, pY);

		this.mAnimationOutDirection = pAnimationOutDirection;
		this.mAnimationInDirection = pAnimationInDirection;
		this.mEasingOut = pEasingOut;
		this.mEasingIn = pEasingIn;
		this.mAnimatioTimeIn = pAnimationTimeIn;
		this.mAnimatioTimeOut = pAnimationTimeOut;
		this.mContainerAnimator = new EntityAnimator(getContainer());
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public void setValue(T pValue) {
		this.mPendingValue = pValue;
		this.mValue = pValue;
	}
	public T getValue() {
		return this.mValue;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public abstract void updateValue(T pValue);

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		if (this.mPendingValue != null && !isAnimating()) {
			this.mAnimateOutListener.mAnimationTimeIn = this.mAnimatioTimeIn;
			animateOut(this.mAnimatioTimeOut, this.mAnimateOutListener);
		}
		super.onManagedUpdate(pSecondsElapsed);
	}
	// ===========================================================
	// Methods
	// ===========================================================


	protected void animateOut(final float pTime, final IAnimatorListener pListener) {
		final IEntity container = getContainer();
		final float distance = calculateDistance(this.mAnimationOutDirection);
		PositionerSceneRelative.getInstance().center(getWindow(), container);
		animate(container, this.mAnimationOutDirection, distance, pTime, this.mEasingOut, pListener);
	}

	protected void animateIn(final float pTime, final IAnimatorListener pListener) {
		final IEntity container = getContainer();
		final float distance = calculateDistance(this.mAnimationInDirection);
		position(container, this.mAnimationInDirection);
		animate(container, this.mAnimationInDirection, distance, pTime, this.mEasingIn, pListener);
	}

	protected float calculateDistance(eAnimationDirection pAnimationDirection) {
		if (pAnimationDirection.isHorizontal) {
			return getWindow().getWidth()/2 + getContainer().getWidth()/2;
		} else {
			return getWindow().getHeight()/2 + getContainer().getHeight()/2;
		}
	}

	protected boolean isAnimating() {
		return this.mContainerAnimator.isRunning();
	}

	protected void position(IEntity pEntity, eAnimationDirection pDirection) {
		switch (pDirection) {
		case UP:
			PositionerImmovableRelative.getInstance().placeBelowOfAndCenter(getWindow(), pEntity);
			break;
		case DOWN:
			PositionerImmovableRelative.getInstance().placeTopOfAndCenter(getWindow(), pEntity);
			break;
		case LEFT:
			PositionerImmovableRelative.getInstance().placeRightOfAndCenter(getWindow(), pEntity);
			break;
		case RIGHT:
			PositionerImmovableRelative.getInstance().placeLeftOfAndCenter(getWindow(), pEntity);
			break;
		}
	}

	protected void animate(IEntity pEntity, eAnimationDirection pDirection, final float pDistance, final float pTime, IEaseFunction pEasing, IAnimatorListener pListener) {
		float x = pEntity.getX();
		float y = pEntity.getY();
		switch (pDirection) {
		case UP:
			this.mContainerAnimator.run(new MoveModifier(pTime , x, y, x, y + pDistance, pEasing), pListener);
			break;
		case DOWN:
			this.mContainerAnimator.run(new MoveModifier(pTime, x, y, x, y - pDistance, pEasing), pListener);
			break;
		case LEFT:
			this.mContainerAnimator.run(new MoveModifier(pTime, x, y, x - pDistance, y, pEasing), pListener);
			break;
		case RIGHT:
			this.mContainerAnimator.run(new MoveModifier(pTime, x, y, x + pDistance, y, pEasing), pListener);
			break;
		}
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public static enum eAnimationDirection {
		UP(false), DOWN(false), LEFT(true), RIGHT(true);

		public final boolean isHorizontal;
		private eAnimationDirection(boolean pHorizontal) {
			this.isHorizontal = pHorizontal;
		}
	};

	protected class AnimationOutListener implements IAnimatorListener {
		public float mAnimationTimeIn;
		private AnimationOutListener(float pAnimatioTimeIn) {
			this.mAnimationTimeIn = pAnimatioTimeIn;
		}

		@Override
		public void onAnimatorFinished(EntityAnimator pAnimator) {
			if (Swing.this.mPendingValue != null) {
				updateValue(Swing.this.mPendingValue);
				Swing.this.mPendingValue = null;
				animateIn(this.mAnimationTimeIn, null);
			}
		}
	}
}
