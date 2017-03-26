// Copyright 2012 Square, Inc.
package com.squareup.timessquare;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MonthView extends LinearLayout {
    TextView title;
    CalendarGridView grid;
    private Listener listener;
    private List<CalendarCellDecorator> decorators;
    private boolean isRtl;
    private int countY = 0;
    private int countM = 0;
    private int countD = 0;
    private List<Integer> cs = new ArrayList<Integer>();
    // private int position = Integer.MAX_VALUE;
    //
    //
    // public int getPosition() {
    // return position;
    // }
    //
    // public void setPosition(int position) {
    // this.position = position;
    // }
    private int SPposition = Integer.MAX_VALUE;
    private int EPposition = Integer.MAX_VALUE;
    private int SMPposition = Integer.MAX_VALUE;
    private int EMPposition = Integer.MAX_VALUE;
    private int SYPposition = Integer.MAX_VALUE;
    private int EYPposition = Integer.MAX_VALUE;


    public void setSYPposition(int sYPposition) {
        SYPposition = sYPposition;
    }


    public void setEYPposition(int eYPposition) {
        EYPposition = eYPposition;
    }


    public void setSMPposition(int sMPposition) {
        SMPposition = sMPposition;
    }


    public void setEMPposition(int eMPposition) {
        EMPposition = eMPposition;
    }

    private String s;

    public void setSPposition(int sPposition) {
        SPposition = sPposition;
    }

    public void setEPposition(int ePposition) {
        EPposition = ePposition;
    }

    public static MonthView create(ViewGroup parent, LayoutInflater inflater,
                                   DateFormat weekdayNameFormat, Listener listener, Calendar today,
                                   int dividerColor, int dayBackgroundResId, int dayTextColorResId,
                                   int titleTextColor, boolean displayHeader, int headerTextColor,
                                   Locale locale) {
        return create(parent, inflater, weekdayNameFormat, listener, today,
                dividerColor, dayBackgroundResId, dayTextColorResId,
                titleTextColor, displayHeader, headerTextColor, null, locale);
    }

    public static MonthView create(ViewGroup parent, LayoutInflater inflater,
                                   DateFormat weekdayNameFormat, Listener listener, Calendar today,
                                   int dividerColor, int dayBackgroundResId, int dayTextColorResId,
                                   int titleTextColor, boolean displayHeader, int headerTextColor,
                                   List<CalendarCellDecorator> decorators, Locale locale) {
        final MonthView view = (MonthView) inflater.inflate(R.layout.month,
                parent, false);
        view.setDividerColor(dividerColor);
        view.setDayTextColor(dayTextColorResId);
        view.setTitleTextColor(titleTextColor);
        view.setDisplayHeader(displayHeader);
        view.setHeaderTextColor(headerTextColor);

        if (dayBackgroundResId != 0) {
            view.setDayBackground(dayBackgroundResId);
        }

        final int originalDayOfWeek = today.get(Calendar.DAY_OF_WEEK);

        view.isRtl = isRtl(locale);
        int firstDayOfWeek = today.getFirstDayOfWeek();
        final CalendarRowView headerRow = (CalendarRowView) view.grid
                .getChildAt(0);
        for (int offset = 0; offset < 7; offset++) {
            today.set(Calendar.DAY_OF_WEEK,
                    getDayOfWeek(firstDayOfWeek, offset, view.isRtl));
            final TextView textView = (TextView) headerRow.getChildAt(offset);
            textView.setText(weekdayNameFormat.format(today.getTime()));
        }
        today.set(Calendar.DAY_OF_WEEK, originalDayOfWeek);
        view.listener = listener;
        view.decorators = decorators;
        return view;
    }

    private static int getDayOfWeek(int firstDayOfWeek, int offset,
                                    boolean isRtl) {
        int dayOfWeek = firstDayOfWeek + offset;
        if (isRtl) {
            return 8 - dayOfWeek;
        }
        return dayOfWeek;
    }

    private static boolean isRtl(Locale locale) {
        // of this (on 17+)?
        final int directionality = Character.getDirectionality(locale
                .getDisplayName(locale).charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT
                || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDecorators(List<CalendarCellDecorator> decorators) {
        this.decorators = decorators;
    }

    public List<CalendarCellDecorator> getDecorators() {
        return decorators;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        title = (TextView) findViewById(R.id.title);
        grid = (CalendarGridView) findViewById(R.id.calendar_grid);
    }

    public void init(MonthDescriptor month,
                     List<List<MonthCellDescriptor>> cells, boolean displayOnly,
                     Typeface titleTypeface, Typeface dateTypeface,
                     CalendarPickerView.SelectionMode mode,
                     Date beginDate, Date endDate, Set<Date> blueDates, Set<Date> orangeDates) {
        Logr.d("Initializing MonthView (%d) for %s",
                System.identityHashCode(this), month);
        long start = System.currentTimeMillis();
        // title.setText(month.getLabel());
        title.setText(month.getYear() + "年" + (month.getMonth() + 1) + "月");
        final int numRows = cells.size();
        grid.setNumRows(numRows);
        for (int i = 0; i < 6; i++) {
            CalendarRowView weekRow = (CalendarRowView) grid.getChildAt(i + 1);
            weekRow.setListener(listener);
            if (i < numRows) {
                weekRow.setVisibility(VISIBLE);
                List<MonthCellDescriptor> week = cells.get(i);
                for (int c = 0; c < week.size(); c++) {
                    MonthCellDescriptor cell = week.get(isRtl ? 6 - c : c);
                    CalendarCellView cellView = (CalendarCellView) weekRow
                            .getChildAt(c);

                    String cellDate = Integer.toString(cell.getValue());
                    if (!cellView.getText().equals(cellDate)) {
                        cellView.setText(cellDate);
                    }
                    cellView.setEnabled(cell.isCurrentMonth());
                    cellView.setClickable(!displayOnly);
                    cellView.setSelectable(cell.isSelectable());
                    cellView.setSelected(cell.isSelected());
                    cellView.setCurrentMonth(cell.isCurrentMonth());
                    cellView.setToday(cell.isToday());
                    cellView.setRangeState(cell.getRangeState());
                    cellView.setHighlighted(cell.isHighlighted());
                    cellView.setTag(cell);
                    Date date = cell.getDate();

//                    if(date.getDay() == 6 || date.getDay() == 0 &&
//                            cellView.getCurrentTextColor()!= getResources().getColor(R.color.calendar_active_month_bg)){
//                        cellView.setTextColor(getResources().getColor(R.color.calendar_week_sun));
//                    }

                    if(! cell.isCurrentMonth()){
                        cellView.setBackgroundResource(R.color.calendar_active_month_bg);
                        cellView.setTextColor(getResources().getColor(R.color.calendar_active_month_bg));
                    }
                    if(cell.isSelectable()){
                        cellView.setBackgroundResource(R.color.calendar_active_month_bg);
                        cellView.setTextColor(getResources().getColor(R.color.calendar_selcted_black));
                    }
                    if (CalendarPickerView.SelectionMode.MULTIPLE == mode) {
                        if (cell.isSelected()) {
                            cellView.setBackgroundResource(R.drawable.reserve_choose_icon);
                            cellView.setTextColor(getResources().getColor(R.color.calendar_selcted_black));
                        } else {
                            if (cellView.getCurrentTextColor() != getResources().getColor(R.color.calendar_active_month_bg)) {
                                cellView.setBackgroundResource(R.color.calendar_no_color);
                                cellView.setTextColor(getResources().getColor(R.color.calendar_selcted_black));
                            }
                        }
                        if (null != beginDate && null != endDate) {
                            if (date.getTime() + (23 * 60 * 60 * 1000 + 59 * 60 * 1000) >= beginDate.getTime() &&
                                    date.getTime() <= endDate.getTime() &&
                                    cellView.getCurrentTextColor() != getResources().getColor(R.color.calendar_active_month_bg)
                                    ) {
                                cellView.setTextColor(getResources().getColor(R.color.calendar_selcted_black));
                            } else {
                                if (cellView.getCurrentTextColor() != getResources().getColor(R.color.calendar_active_month_bg)) {
                                    cellView.setTextColor(getResources().getColor(R.color.calendar_week_color));
                                }
                            }
                            if (date.getYear() == beginDate.getYear() &&
                                    date.getMonth() == beginDate.getMonth() &&
                                    date.getDate() == beginDate.getDate()) {
                                cellView.setTextColor(getResources().getColor(R.color.calendar_selected_day_bg));
                            }
                        }
                        if (null != orangeDates && orangeDates.size() > 0) {
                            for (Date oDate : orangeDates) {
                                if (date.getYear() == oDate.getYear() &&
                                        date.getMonth() == oDate.getMonth() &&
                                        date.getDate() == oDate.getDate() &&(
                                        cellView.getCurrentTextColor() == getResources().getColor(R.color.calendar_selcted_black)||
                                                cellView.getCurrentTextColor() == getResources().getColor(R.color.calendar_selected_day_bg))&&
                                        cellView.getCurrentTextColor() != getResources().getColor(R.color.calendar_active_month_bg)) {
                                    cellView.setBackgroundResource(R.drawable.reserve_ban_icon);
                                    cellView.setTextColor(getResources().getColor(R.color.calendar_selcted_black));
                                }
                            }
                        }
                        if (null != blueDates && blueDates.size() > 0) {
                            for (Date bDate : blueDates) {
                                if (date.getYear() == bDate.getYear() &&
                                        date.getMonth() == bDate.getMonth() &&
                                        date.getDate() == bDate.getDate() &&(
                                        cellView.getCurrentTextColor() == getResources().getColor(R.color.calendar_selcted_black)||
                                                cellView.getCurrentTextColor() == getResources().getColor(R.color.calendar_selected_day_bg))&&
                                        cellView.getCurrentTextColor() != getResources().getColor(R.color.calendar_active_month_bg)) {
                                    cellView.setBackgroundResource(R.drawable.reserve_adjustment_icon);
                                    cellView.setTextColor(getResources().getColor(R.color.calendar_blue_white));
                                }
                            }
                        }
                    } else {
                        if (null != beginDate && null != endDate) {
                            if (date.getTime() + (23 * 60 * 60 * 1000 + 59 * 60 * 1000) >= beginDate.getTime() &&
                                    date.getTime() <= endDate.getTime() &&
                                    cellView.getCurrentTextColor() != getResources().getColor(R.color.calendar_active_month_bg)) {
                                cellView.setTextColor(getResources().getColor(R.color.calendar_selcted_black));
                            } else {
                                if (cellView.getCurrentTextColor() != getResources().getColor(R.color.calendar_active_month_bg)) {
                                    cellView.setTextColor(getResources().getColor(R.color.calendar_week_color));
                                }
                            }
                            if (date.getYear() == beginDate.getYear() &&
                                    date.getMonth() == beginDate.getMonth() &&
                                    date.getDate() == beginDate.getDate() && cell.isCurrentMonth()) {
                                cellView.setTextColor(getResources().getColor(R.color.calendar_selected_day_bg));
                            }
                        }
                        if (cell.isSelected()) {
                            countD = cell.getDate().getDate();
                            countM = cell.getDate().getMonth();
                            countY = cell.getDate().getYear();
                            cellView.setTextColor(getResources().getColor(R.color.calendar_blue_white));
                            cellView.setBackgroundResource(R.color.calendar_selcted_range);
                            if (SPposition != Integer.MAX_VALUE
                                    || EPposition != Integer.MAX_VALUE) {
                                if (SPposition == EPposition && SMPposition == EMPposition && SYPposition == EYPposition) {
                                    cellView.setBackgroundColor(getResources()
                                            .getColor(
                                                    R.color.calendar_selected_day_bg));
                                } else if (countD == SPposition && countM == SMPposition && countY == SYPposition) {
                                    cellView.setBackgroundResource(R.drawable.order_startchoose_btn);
                                    SPposition = Integer.MAX_VALUE;
                                    SMPposition = Integer.MAX_VALUE;
                                    SYPposition = Integer.MAX_VALUE;

                                    Logr.d("开始");
                                } else if (countD == EPposition && countM == EMPposition && countY == EYPposition) {
                                    cellView.setBackgroundResource(R.drawable.order_endchoose_btn);
                                    EPposition = Integer.MAX_VALUE;
                                    EMPposition = Integer.MAX_VALUE;
                                    EYPposition = Integer.MAX_VALUE;
                                    Logr.d("结束");
                                }
                            }
                        } else {
                            cellView.setBackgroundResource(R.color.calendar_no_color);
                            if (cellView.getCurrentTextColor() != getResources().getColor(R.color.calendar_week_color)&&
                                    cellView.getCurrentTextColor() != getResources().getColor(R.color.calendar_selected_day_bg)
                                    && cellView.getCurrentTextColor() != getResources().getColor(R.color.calendar_active_month_bg)){
                                cellView.setTextColor(getResources().getColor(R.color.calendar_selcted_black));
                            }
                        }
                    }

                    if (null != decorators) {
                        for (CalendarCellDecorator decorator : decorators) {
                            decorator.decorate(cellView, cell.getDate());
                        }
                    }
                }
            } else {
                weekRow.setVisibility(GONE);
            }
        }

        if (titleTypeface != null) {
            title.setTypeface(titleTypeface);
        }
        if (dateTypeface != null) {
            grid.setTypeface(dateTypeface);
        }

        Logr.d("MonthView.init took %d ms", System.currentTimeMillis() - start);
    }

    public void setDividerColor(int color) {
        grid.setDividerColor(color);
    }

    public void setDayBackground(int resId) {
        grid.setDayBackground(resId);
    }

    public void setDayTextColor(int resId) {
        grid.setDayTextColor(resId);
    }

    public void setTitleTextColor(int color) {
        title.setTextColor(color);
    }

    public void setDisplayHeader(boolean displayHeader) {
        grid.setDisplayHeader(displayHeader);
    }

    public void setHeaderTextColor(int color) {
        grid.setHeaderTextColor(color);
    }

    public interface Listener {
        void handleClick(MonthCellDescriptor cell);
    }
}
