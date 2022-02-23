package ru.atm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class SafeAtm implements Safe {

    private final Map<Banknote, Integer> slots;

    private final Logger logger;

    public SafeAtm(Map<Banknote, Integer> slots) {
        this.slots = slots;
        this.logger = Logger.getLogger(SafeAtm.class.getName());
    }

    @Override
    public void add(Banknote banknote) {
        if (slots.containsKey(banknote)) {
            var value = slots.get(banknote);
            slots.put(banknote, ++value);
        } else {
            throw new MyRuntimeException("There're not the such banknote!");
        }
    }

    @Override
    public List<Banknote> giveMoney(String accountNumber, BigDecimal amount) {

        var tempSlots = new HashMap<Banknote, Integer>();

        var amountInt = amount.intValue();

        for (Map.Entry<Banknote, Integer> pair : slots.entrySet()) {

            var note = pair.getKey().getNote();

            if (amountInt >= note && slots.get(pair.getKey()) != 0) {

                var countVal = amountInt / note;

                amountInt = amountInt - countVal * note;

                slots.put(pair.getKey(), pair.getValue() - countVal);

                tempSlots.put(pair.getKey(), countVal);

                if (amountInt == 0) break;
            }
        }

        return getBanknotes(tempSlots);
    }


    private ArrayList<Banknote> getBanknotes(HashMap<Banknote, Integer> tempSlots) {
        var tempList = new ArrayList<Banknote>();

        tempSlots.forEach((k, v) -> {
            IntStream.range(0, v).forEach(e -> tempList.add(k));
        });
        return tempList;
    }

    @Override
    public void showBalance() {
        if (this.slots != null) {
            this.slots.forEach((key, value) ->
                    logger.info(String.format("Slot name: %s count banknote: %d", key, value)));
        } else {
            throw new MyRuntimeException("Slots aren't init");
        }
    }

    @Override
    public Map<Banknote, Integer> getSlots() {
        return this.slots;
    }

}
