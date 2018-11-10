package util;

class Motivation {
    private static final String[] ask_list = {
            "Tell you a blatant lie, because I don't like you or because I'm afraid.",
            "Tell you a half-truth, because I don't trust you or because I'm afraid.",
            "Tell you the truth, but mix up all the details, because I want to trust you, but I'm afraid.",
            "Tell you the truth, because I like and trust you, or I need help.",
    };
    private static final String[] motivation_list = {
            "To gain wealth.",
            "To gain power.",
            "To fulfill a promise.",
            "To get revenge.",
            "To get justice.",
            "For the glory.",
            "To gain the favor of someone.",
            "To help a friend.",
            "To stop an event.",
            "To pay a debt.",
            "To solve a mystery.",
            "To escape from a problem.",
            "To thwart an enemy.",
            "To expose the truth.",
            "To cover up the truth.",
            "To gain my freedom.",
            "To liberate others.",
            "To oppress others.",
            "To keep the status quo.",
            "To upset the status quo.",
    };
    private static final String[] stop_list = {
            "As soon as I meet a serious setback or if I, or anyone else gets hurt.",
            "As soon as there are a lot of obstacles, or if I, or anyone else gets seriously hurt.",
            "As soon as someone dies.",
            "Never. I will die before I give up.",
    };
    private final String ask;
    private final String motivation;
    private final String stop;

    public Motivation() {
        ask = Weight.choose(ask_list);
        motivation = Weight.choose(motivation_list);
        stop = Weight.choose(stop_list);
    }

    @Override
    public String toString() {
        String sb = "You asked, so I'll:\n" + ask + '\n' +
                "Why am I doing this?\n" +
                motivation + '\n' +
                "When will I stop?\n" +
                stop + '\n';
        return sb;
    }

}
