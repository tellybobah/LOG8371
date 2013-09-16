package com.pmease.commons.wicket.editable.reflection;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.pmease.commons.wicket.editable.RenderableEditContext;

@SuppressWarnings("serial")
public class ReflectionPropertyEditor extends Panel {

	private final ReflectionPropertyEditContext editContext;
	
	public ReflectionPropertyEditor(String id, ReflectionPropertyEditContext editContext) {
		super(id);
		this.editContext = editContext;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		if (editContext.isPropertyRequired()) {
			add(new WebMarkupContainer("enable").setVisible(false));
			if (editContext.getPropertyValue() == null) {
				editContext.setPropertyValue(editContext.instantiate(editContext.getPropertyGetter().getReturnType()));
			}
		} else {
			add(new CheckBox("enable", new IModel<Boolean>() {
				
				@Override
				public void detach() {
					
				}
	
				@Override
				public Boolean getObject() {
					return editContext.getPropertyValue() != null;
				}
	
				@Override
				public void setObject(Boolean object) {
					if (object) {
						editContext.setPropertyValue(editContext.instantiate(editContext.getPropertyGetter().getReturnType()));
					} else {
						editContext.setPropertyValue(null);
					}
				}
				
			}).add(new AjaxFormComponentUpdatingBehavior("onclick"){

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					Component editor = newPropertyValueEditor();
					replace(editor);
					target.add(editor);
				}
				
			}));
			
		}

		add(newPropertyValueEditor());
	}
	
	private Component newPropertyValueEditor() {
		Component editor;
		RenderableEditContext valueContext = (RenderableEditContext) editContext.getValueContext();
		if (valueContext != null) {
			editor = valueContext.renderForEdit("valueEditor");
		} else {
			editor = new WebMarkupContainer("valueEditor").setVisible(false);
		}
		editor.setOutputMarkupId(true);
		editor.setOutputMarkupPlaceholderTag(true);
		return editor;
	}

}
