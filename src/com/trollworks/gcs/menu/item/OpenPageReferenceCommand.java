/*
 * Copyright (c) 1998-2016 by Richard A. Wilkes. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * version 2.0. If a copy of the MPL was not distributed with this file, You
 * can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * This Source Code Form is "Incompatible With Secondary Licenses", as defined
 * by the Mozilla Public License, version 2.0.
 */

package com.trollworks.gcs.menu.item;

import com.trollworks.gcs.common.HasSourceReference;
import com.trollworks.gcs.library.LibraryExplorerDockable;
import com.trollworks.gcs.pdfview.PdfDockable;
import com.trollworks.gcs.preferences.ReferenceLookupPreferences;
import com.trollworks.toolkit.annotation.Localize;
import com.trollworks.toolkit.ui.Selection;
import com.trollworks.toolkit.ui.menu.Command;
import com.trollworks.toolkit.ui.widget.StdFileDialog;
import com.trollworks.toolkit.ui.widget.outline.Outline;
import com.trollworks.toolkit.ui.widget.outline.OutlineModel;
import com.trollworks.toolkit.ui.widget.outline.Row;
import com.trollworks.toolkit.utility.FileType;
import com.trollworks.toolkit.utility.Localization;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Path;

import javax.swing.filechooser.FileNameExtensionFilter;

/** Provides the "Open Page Reference" command. */
public class OpenPageReferenceCommand extends Command {
	@Localize("Open Page Reference")
	private static String	OPEN_PAGE_REFERENCE;
	@Localize("Locate the PDF file for the prefix \"%s\"")
	private static String	LOCATE_PDF;
	@Localize("PDF File")
	private static String	PDF_FILE;

	static {
		Localization.initialize();
	}

	/** The action command this command will issue. */
	public static final String						CMD_OPEN_PAGE_REFERENCE	= "OpenPageReference";				//$NON-NLS-1$

	/** The singleton {@link OpenPageReferenceCommand}. */
	public static final OpenPageReferenceCommand	INSTANCE				= new OpenPageReferenceCommand();

	private OpenPageReferenceCommand() {
		super(OPEN_PAGE_REFERENCE, CMD_OPEN_PAGE_REFERENCE, KeyEvent.VK_G);
	}

	@Override
	public void adjust() {
		setEnabled(!getReference().trim().isEmpty());
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String reference = getReference();
		if (!reference.isEmpty()) {
			int i = reference.length() - 1;
			while (i >= 0) {
				char ch = reference.charAt(i);
				if (ch >= '0' && ch <= '9') {
					i--;
				} else {
					i++;
					break;
				}
			}
			if (i > 0) {
				String id = reference.substring(0, i);
				try {
					int page = Integer.parseInt(reference.substring(i));
					File file = ReferenceLookupPreferences.getPdfLocation(id);
					if (file == null) {
						file = StdFileDialog.showOpenDialog(getFocusOwner(), String.format(LOCATE_PDF, id), new FileNameExtensionFilter(PDF_FILE, FileType.PDF_EXTENSION));
						if (file != null) {
							ReferenceLookupPreferences.setPdfLocation(id, file);
						}
					}
					if (file != null) {
						Path path = file.toPath();
						LibraryExplorerDockable library = LibraryExplorerDockable.get();
						PdfDockable dockable = (PdfDockable) library.getDockableFor(path);
						if (dockable != null) {
							dockable.goToPage(page);
						} else {
							dockable = new PdfDockable(file, page);
							library.dockPdf(dockable);
							library.open(path);
						}
					}
				} catch (NumberFormatException nfex) {
					// Ignore
				}
			}
		}
	}

	private static String getReference() {
		Component comp = getFocusOwner();
		if (comp instanceof Outline) {
			OutlineModel model = ((Outline) comp).getModel();
			if (model.hasSelection()) {
				Selection selection = model.getSelection();
				if (selection.getCount() == 1) {
					Row row = model.getFirstSelectedRow();
					if (row instanceof HasSourceReference) {
						String[] refs = ((HasSourceReference) row).getReference().split(","); //$NON-NLS-1$
						if (refs.length > 0) {
							return refs[0].trim();
						}
					}
				}
			}
		}
		return ""; //$NON-NLS-1$
	}
}