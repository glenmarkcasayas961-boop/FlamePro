# Responsive Layout Implementation Plan (XML)

The goal is to make the main dashboard responsive, optimizing it for both standard phones and larger screens (tablets/foldables) using traditional Android XML techniques.

## User Review Required

> [!IMPORTANT]
> This plan uses **resource qualifiers** (`layout-w600dp`) to provide a different layout for larger screens.
> I will introduce a `NavigationRailView` for tablets, which is the Material Design recommendation for wider displays.

## Proposed Changes

### Configuration & Resources

#### [NEW] [dimens.xml](file:///C:/Users/Casay/AndroidStudioProjects/FlamePro/app/src/main/res/values/dimens.xml)
Define standard margins and text sizes to be used across layouts.

#### [NEW] [dimens.xml (w600dp)](file:///C:/Users/Casay/AndroidStudioProjects/FlamePro/app/src/main/res/values-w600dp/dimens.xml)
Override dimensions for larger screens (e.g., larger padding, different layout weights).

---

### UI Layer

#### [MODIFY] [activity_main.xml](file:///C:/Users/Casay/AndroidStudioProjects/FlamePro/app/src/main/res/layout/activity_main.xml)
Refactor the current layout to ensure it remains a clean "phone" layout while preparing for adaptive changes.

#### [NEW] [activity_main.xml (w600dp)](file:///C:/Users/Casay/AndroidStudioProjects/FlamePro/app/src/main/res/layout-w600dp/activity_main.xml)
Create a tablet-optimized layout featuring:
- A lateral `NavigationRailView` instead of a `BottomNavigationView`.
- More efficient use of horizontal space (e.g., side-by-side components).

---

### Logic Layer

#### [MODIFY] [MainActivity.java](file:///C:/Users/Casay/AndroidStudioProjects/FlamePro/app/src/main/java/com/example/flamepro/MainActivity.java)
Update the activity to:
- Detect which navigation view is present (`BottomNavigationView` or `NavigationRailView`).
- Initialize and set listeners for the active navigation component.
- Adjust `RecyclerView` configurations (like span count) based on screen width.

## Verification Plan

### Automated Tests
- I will verify if both layouts compile and can be rendered.

### Manual Verification
- Deploy the app to a Phone emulator and verify the bottom navigation.
- Deploy the app to a Tablet emulator and verify the navigation rail and adaptive dashboard.
