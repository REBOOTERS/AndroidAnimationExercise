package home.smart.fly.animationdemo.customview.wheel.widget.adapter;

/**
 * Numeric Wheel adapter.
 */
public class NumericWheelAdapter implements WheelAdapter
{

	/** The default min value */
	public static final int DEFAULT_MAX_VALUE = 9;

	/** The default max value */
	private static final int DEFAULT_MIN_VALUE = 0;

	// Values
	private int minValue;
	private int maxValue;

	// format
	private String format;

	/**
	 * Default constructor
	 */
	public NumericWheelAdapter()
	{
		this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
	}

	/**
	 * Constructor
	 *
	 * @param minValue
	 *            the wheel min value
	 * @param maxValue
	 *            the wheel max value
	 */
	public NumericWheelAdapter(int minValue, int maxValue)
	{
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	/**
	 * Constructor
	 *
	 * @param minValue
	 *            the wheel min value
	 * @param maxValue
	 *            the wheel max value
	 * @param format
	 *            the format string
	 */
	public NumericWheelAdapter(int minValue, int maxValue, String format)
	{
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.format = format;
	}

	@Override
	public Object getItem(int index)
	{
		if (index >= 0 && index < getItemsCount())
		{
			int value = minValue + index;
			return format != null ? String.format(format, value) : Integer.toString(value);
		}
		return 0;
	}

	@Override
	public int getItemsCount()
	{
		return maxValue - minValue + 1;
	}

	@Override
	public int indexOf(Object o)
	{
		return Integer.parseInt(o.toString());
	}
}
