package karasu_lab.mcmidi.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtType;
import net.minecraft.nbt.scanner.NbtScanner;
import net.minecraft.nbt.visitor.NbtElementVisitor;

import javax.sound.midi.Sequencer;
import java.io.DataOutput;
import java.io.IOException;

public abstract class CustomSequencer implements Sequencer {
    private final NbtCompound nbtCompound;

    public CustomSequencer() {
        this.nbtCompound = new NbtCompound();
    }

    public CustomSequencer(NbtCompound nbtCompound) {
        this.nbtCompound = nbtCompound;
    }

    public NbtCompound getNbtCompound() {
        return nbtCompound;
    }
}
