package ru.hwAtm.safe.safeImpl;

import ru.hwAtm.banknote.Banknote;
import ru.hwAtm.exception.MyRuntimeException;
import ru.hwAtm.safe.Safe;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class SimpleSafe implements Safe {

    private final Map<Banknote, Integer> slots;

    private final Map<Banknote, Integer> tempSlots = new HashMap<>();

    private final Logger logger;

    public SimpleSafe() {
        this.slots = new TreeMap<>(
                Comparator.comparing(Banknote::getCurrency)
                        .thenComparing(Banknote::getNote).reversed());

        this.logger = Logger.getLogger(SimpleSafe.class.getName());
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

    @Override
    public Map<Banknote, Integer> getSlots() {
        return this.slots;
    }

    public void uploadBanknotes(Banknote banknote, Integer value) {
        slots.put(banknote, value);
    }


    private List<Banknote> getBanknotes(Map<Banknote, Integer> tempSlots) {
        var tempList = new ArrayList<Banknote>();

        tempSlots.forEach((k, v) -> {
            IntStream.range(0, v).forEach(e -> tempList.add(k));
        });
        return tempList;
    }
}
