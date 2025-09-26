package sumdu.edu.ua;

import java.util.*;
import java.text.*;

/** Containing items and calculating price. */
public class ShoppingCart{

    public static enum ItemType { NEW, REGULAR, SECOND_FREE, SALE }

    // Constants
    private static final String NO_ITEMS_MESSAGE = "No items.";
    private static final String DISCOUNT_PLACEHOLDER = "-";
    private static final double MIN_PRICE = 0.01;
    private static final int MAX_TITLE_LENGTH = 32;
    private static final int MIN_QUANTITY = 1;
    private static final int MAX_DISCOUNT = 80;
    private static final int DISCOUNT_STEP = 10;

    /**
     * Container for added items
     */
    private final List<Item> items = new ArrayList<>();

    /**
     * Tests all class methods.
     */
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("Apple", 0.99, 5, ItemType.NEW);
        cart.addItem("Banana", 20.00, 4, ItemType.SECOND_FREE);
        cart.addItem("A long piece of toilet paper", 17.20, 1, ItemType.SALE);
        cart.addItem("Nails", 2.00, 500, ItemType.REGULAR);
        System.out.println(cart.formatTicket());
    }

    /**
     * Adds new item.
     * @param title item title 1 to 32 symbols
     * @param price item price in USD, > 0
     * @param quantity item quantity, from 1
     * @param type item type
     * @throws IllegalArgumentException if some value is wrong
     */
    public void addItem(String title, double price, int quantity, ItemType type){
        validateItemInput(title, price, quantity, type);

        Item item = new Item(title, price, quantity, type);
        items.add(item);
    }

    private void validateItemInput(String title, double price, int quantity, ItemType type) {
        if (title == null || title.trim().isEmpty() || title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("Illegal title");
        }
        if (price < MIN_PRICE) {
            throw new IllegalArgumentException("Illegal price");
        }
        if (quantity < MIN_QUANTITY) {
            throw new IllegalArgumentException("Illegal quantity");
        }
        if (type == null) {
            throw new IllegalArgumentException("ItemType cannot be null");
        }
    }

    /**
     * Formats shopping ticket.
     * @return formatted ticket string or "No items." if cart is empty
     */
    public String formatTicket(){
        if (items.isEmpty()) {
            return NO_ITEMS_MESSAGE;
        }

        double total = calculateItemsParameters();
        return getFormattedTicketTable(total);
    }

    /**
     * Calculates discount and total price for each item and returns overall total
     */
    private double calculateItemsParameters() {
        double total = 0.00;
        for (Item item : items) {
            int discount = calculateDiscount(item.getItemType(), item.getQuantity());
            item.setDiscount(discount);
            double itemTotal = item.getPrice() * item.getQuantity() * (100.0 - discount) / 100.0;
            item.setTotalPrice(itemTotal);
            total += itemTotal;
        }
        return total;
    }

    /**
     * Formats and returns the ticket table
     */
    private String getFormattedTicketTable(double total) {
        TableFormatter formatter = new TableFormatter();
        return formatter.formatTable(convertItemsToTableLines(), total, items.size());
    }

    /**
     * Converts items to table lines for formatting
     */
    private List<String[]> convertItemsToTableLines() {
        List<String[]> lines = new ArrayList<>();
        int index = 0;
        for (Item item : items) {
            lines.add(new String[]{
                    String.valueOf(++index),
                    item.getTitle(),
                    MONEY.format(item.getPrice()),
                    String.valueOf(item.getQuantity()),
                    formatDiscount(item.getDiscount()),
                    MONEY.format(item.getTotalPrice())
            });
        }
        return lines;
    }

    private String formatDiscount(int discount) {
        return discount == 0 ? DISCOUNT_PLACEHOLDER : discount + "%";
    }

    /**
     * Calculates item's discount.
     */
    public static int calculateDiscount(ItemType type, int quantity){
        if (type == null) {
            throw new IllegalArgumentException("ItemType cannot be null");
        }

        int discount = getBaseDiscount(type, quantity);
        return applyQuantityDiscount(discount, quantity, type);
    }

    private static int getBaseDiscount(ItemType type, int quantity) {
        switch (type) {
            case NEW:
                return 0;
            case REGULAR:
                return 0;
            case SECOND_FREE:
                return quantity > 1 ? 50 : 0;
            case SALE:
                return 70;
            default:
                throw new IllegalArgumentException("Unknown ItemType: " + type);
        }
    }

    private static int applyQuantityDiscount(int baseDiscount, int quantity, ItemType type) {
        if (type == ItemType.NEW || baseDiscount >= MAX_DISCOUNT) {
            return baseDiscount;
        }

        int additionalDiscount = quantity / DISCOUNT_STEP;
        int totalDiscount = baseDiscount + additionalDiscount;
        return Math.min(totalDiscount, MAX_DISCOUNT);
    }

    // --- Table Formatting Helper Class ---
    private static class TableFormatter {
        private static final String[] HEADER = {"#","Item","Price","Quan.","Discount","Total"};
        private static final int[] ALIGN = {1, -1, 1, 1, 1, 1};

        public String formatTable(List<String[]> lines, double total, int itemCount) {
            String[] footer = {String.valueOf(itemCount), "", "", "", "", MONEY.format(total)};

            int[] width = calculateColumnWidths(lines, footer);
            int lineLength = calculateLineLength(width);

            return buildFormattedTable(lines, footer, width, lineLength);
        }

        private int[] calculateColumnWidths(List<String[]> lines, String[] footer) {
            int[] width = new int[HEADER.length];

            adjustColumnWidth(width, HEADER);
            adjustColumnWidth(width, footer);
            for (String[] line : lines) {
                adjustColumnWidth(width, line);
            }

            return width;
        }

        private void adjustColumnWidth(int[] width, String[] columns) {
            for (int i = 0; i < columns.length && i < width.length; i++) {
                width[i] = Math.max(width[i], columns[i].length());
            }
        }

        private int calculateLineLength(int[] width) {
            int lineLength = width.length - 1; // separators between columns
            for (int w : width) {
                lineLength += w;
            }
            return lineLength;
        }

        private String buildFormattedTable(List<String[]> lines, String[] footer,
                                           int[] width, int lineLength) {
            StringBuilder sb = new StringBuilder();

            // Header
            appendFormattedLine(sb, HEADER, ALIGN, width, true);
            appendSeparator(sb, lineLength);

            // Lines
            for (String[] line : lines) {
                appendFormattedLine(sb, line, ALIGN, width, true);
            }

            // Footer separator and footer
            if (!lines.isEmpty()) {
                appendSeparator(sb, lineLength);
            }
            appendFormattedLine(sb, footer, ALIGN, width, false);

            return sb.toString();
        }

        private void appendSeparator(StringBuilder sb, int lineLength) {
            for (int i = 0; i < lineLength; i++) {
                sb.append("-");
            }
            sb.append("\n");
        }

        private void appendFormattedLine(StringBuilder sb, String[] line,
                                         int[] align, int[] width, boolean newLine) {
            for (int i = 0; i < line.length; i++) {
                appendFormatted(sb, line[i], align[i], width[i]);
            }
            if (newLine) {
                sb.append("\n");
            }
        }
    }

    private static final NumberFormat MONEY;
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        MONEY = new DecimalFormat("$#.00", symbols);
    }

    /**
     * Appends formatted value to StringBuilder.
     * Trims string if its length > width.
     * @param align -1 for align left, 0 for center and +1 for align right.
     */
    public static void appendFormatted(StringBuilder sb, String value, int align, int width){
        if (value.length() > width) {
            value = value.substring(0, width);
        }

        int[] padding = calculatePadding(value.length(), align, width);
        int before = padding[0];
        int after = padding[1];

        // Add padding and value
        appendSpaces(sb, before);
        sb.append(value);
        appendSpaces(sb, after);
        sb.append(" "); // trailing space
    }

    private static int[] calculatePadding(int valueLength, int align, int width) {
        int totalPadding = width - valueLength;
        int before, after;

        switch (align) {
            case 0: // center
                before = totalPadding / 2;
                after = totalPadding - before;
                break;
            case -1: // left
                before = 0;
                after = totalPadding;
                break;
            case 1: // right
                before = totalPadding;
                after = 0;
                break;
            default:
                throw new IllegalArgumentException("Invalid align value: " + align);
        }

        return new int[]{before, after};
    }

    private static void appendSpaces(StringBuilder sb, int count) {
        for (int i = 0; i < count; i++) {
            sb.append(" ");
        }
    }

    /** Item info */
    private static class Item {
        private String title;
        private double price;
        private int quantity;
        private ItemType type;
        private int discount;
        private double total;

        public Item(String title, double price, int quantity, ItemType type) {
            this.title = title;
            this.price = price;
            this.quantity = quantity;
            this.type = type;
        }

        // Getters
        public String getTitle() { return title; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public ItemType getItemType() { return type; }
        public int getDiscount() { return discount; }
        public double getTotalPrice() { return total; }

        // Setters (only for calculated values)
        public void setDiscount(int discount) { this.discount = discount; }
        public void setTotalPrice(double total) { this.total = total; }
    }
}