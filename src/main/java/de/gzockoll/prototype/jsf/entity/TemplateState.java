package de.gzockoll.prototype.jsf.entity;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public enum TemplateState {
    EDITABLE{
        @Override
        public TemplateState requestApproval() {
            return transitionTo(READY_FOR_APPROVAL);
        }

        @Override
        public TemplateState assignTransform(Template template, String a) {
            template.setTransform(a);
            return this;
        }

        @Override
        public TemplateState assignStationary(Template template, Asset a) {
            template.setStationery(a);
            return this;
        }
    }, READY_FOR_APPROVAL{
        @Override
        public TemplateState approve() {
            return transitionTo(APPROVED);
        }
    },APPROVED;

    public TemplateState requestApproval() {
        throw new IllegalStateException(getErrorMessage());
    }

    public TemplateState approve() {
        throw new IllegalStateException(getErrorMessage());
    }

    public TemplateState saveContent(Template t, InputStream is) {
        throw new IllegalStateException(getErrorMessage());
    }

    private String getErrorMessage() {
        return "Illegal transistion while in state " + toString();
    }

    public void onExit() {
        log.debug(toString() + ": onExit()");
    }

    public void onEnter() {
        log.debug(toString() + ": onEnter()");
    }

    protected TemplateState transitionTo(TemplateState newState) {
        if (this!=newState) {
            this.onExit();
            newState.onEnter();
        }
        return newState;
    }

    public TemplateState assignTransform(Template template, String a) { throw new IllegalStateException(getErrorMessage()); }

    public TemplateState assignStationary(Template template, Asset a) { throw new IllegalStateException(getErrorMessage()); }
}
