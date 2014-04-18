/*
 * Copyright (c) 1998-2014 by Richard A. Wilkes. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * version 2.0. If a copy of the MPL was not distributed with this file, You
 * can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * This Source Code Form is "Incompatible With Secondary Licenses", as defined
 * by the Mozilla Public License, version 2.0.
 */

package com.trollworks.gcs.advantage;

import com.trollworks.toolkit.annotation.Localize;
import com.trollworks.toolkit.utility.Localization;

/** The types of {@link Advantage} containers. */
public enum AdvantageContainerType {
	/** The standard grouping container type. */
	GROUP {
		@Override
		public String toString() {
			return GROUP_TITLE;
		}
	},
	/**
	 * The meta-trait grouping container type. Acts as one normal trait, listed as an advantage if
	 * its point total is positive, or a disadvantage if it is negative.
	 */
	METATRAIT {
		@Override
		public String toString() {
			return METATRAIT_TITLE;
		}
	},
	/**
	 * The race grouping container type. Its point cost is tracked separately from normal advantages
	 * and disadvantages.
	 */
	RACE {
		@Override
		public String toString() {
			return RACE_TITLE;
		}
	},
	/**
	 * The alternative abilities grouping container type. It behaves similar to a {@link #METATRAIT}
	 * , but applies the rules for alternative abilities (see B61 and P11) to its immediate
	 * children.
	 */
	ALTERNATIVE_ABILITIES {
		@Override
		public String toString() {
			return ALTERNATIVE_ABILITIES_TITLE;
		}
	};

	@Localize("Group")
	static String	GROUP_TITLE;
	@Localize("Meta-Trait")
	static String	METATRAIT_TITLE;
	@Localize("Race")
	static String	RACE_TITLE;
	@Localize("Alternative Abilities")
	static String	ALTERNATIVE_ABILITIES_TITLE;

	static {
		Localization.initialize();
	}
}