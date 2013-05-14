package org.andengine.limbo.widgets.swing;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.limbo.utils.EntityAnimator;
import org.andengine.limbo.utils.EntityAnimator.IAnimatorListener;
import org.andengine.limbo.utils.positioner.PositionerImmovableRelative;
import org.andengine.limbo.widgets.ClippingWindow;
import org.andengine.limbo.widgets.ClippingWindowContainer;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * Base for widgets animating it's value updates. When value of
 * Swing is set by setValue() it hides it's container, updates it's content,
 * and then shows updated container.
 * 
 * Hiding and showing of a container is animated.
 *
 * (c) 2013 Michal Stawinski (nazgee)
 *
 * @author Michal Stawinski
 * @since 20:31:01 - 13.05.2013
 */
public abstract class Swing<T> extends ClippingWindowContainer {
	// ===========================================================
	// Constants
	// ===========================================================
	// ===========================================================
	// Fields
	// ===========================================================
	protected T mValue;
	protected T mPendingValue;

	private final EntityAnimator mContainerAnimator;
	private final AnimationOutListener mAnimateOutListener = new AnimationOutListener(1);

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
	/**
	 * Sets the value of a {@link Swing}. Appropriate animation will be triggered
	 * right after current (if any) animation finishes.
	 * 
	 * @param pValue
	 */
	public void setValue(T pValue) {
		this.mPendingValue = pValue;
		this.mValue = pValue;
	}

	/**
	 * 
	 * @return value set by last setValue() call
	 */
	public T getValue() {
		return this.mValue;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	protected abstract void updateValue(T pValue);

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

	/**
	 * Hides the container by moving it outside of the {@link ClippingWindow} window
	 * 
	 * @param pTime
	 * @param pListener
	 */
	protected void animateOut(final float pTime, final IAnimatorListener pListener) {
		final IEntity container = getContainer();

		container.setPosition(getWindow().getWidth()/2, getWindow().getHeight()/2);
		animate(container, this.mAnimationOutDirection, pTime, this.mEasingOut, pListener);
	}

	/**
	 * Shows the container by moving it back into the {@link ClippingWindow} window
	 * 
	 * @param pTime
	 * @param pListener
	 */
	protected void animateIn(final float pTime, final IAnimatorListener pListener) {
		final IEntity container = getContainer();

		positionForAnimationIn(container, this.mAnimationInDirection);
		animate(container, this.mAnimationInDirection, pTime, this.mEasingIn, pListener);
	}

	
	protected boolean isAnimating() {
		return this.mContainerAnimator.isRunning();
	}

	private void animate(IEntity pEntity, eAnimationDirection pDirection, final float pTime, IEaseFunction pEasing, IAnimatorListener pListener) {
		final float x = pEntity.getX();
		final float y = pEntity.getY();
		final float distance = calculateDistance(pDirection);

		switch (pDirection) {
		case UP:
			this.mContainerAnimator.run(new MoveModifier(pTime , x, y, x, y + distance, pEasing), pListener);
			break;
		case DOWN:
			this.mContainerAnimator.run(new MoveModifier(pTime, x, y, x, y - distance, pEasing), pListener);
			break;
		case LEFT:
			this.mContainerAnimator.run(new MoveModifier(pTime, x, y, x - distance, y, pEasing), pListener);
			break;
		case RIGHT:
			this.mContainerAnimator.run(new MoveModifier(pTime, x, y, x + distance, y, pEasing), pListener);
			break;
		}
	}

	private float calculateDistance(eAnimationDirection pAnimationDirection) {
		if (pAnimationDirection.isHorizontal) {
			return getWindow().getWidth()/2 + getContainer().getWidth()/2;
		} else {
			return getWindow().getHeight()/2 + getContainer().getHeight()/2;
		}
	}

	private void positionForAnimationIn(IEntity pEntity, eAnimationDirection pDirection) {
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	/**
	 * Animation directions supported {@link Swing}
	 * 
	 * @author nazgee
	 *
	 */
	public static enum eAnimationDirection {
		UP(false),
		DOWN(false),
		LEFT(true),
		RIGHT(true);

		public final boolean isHorizontal;
		private eAnimationDirection(boolean pHorizontal) {
			this.isHorizontal = pHorizontal;
		}
	};

	/**
	 * Helper with a sole purpose of starting "in" animation
	 * when current ("out") animation is completed.
	 * 
	 * @author nazgee
	 */
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