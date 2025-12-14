from playwright.sync_api import sync_playwright
import os
import time

def run():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        context = browser.new_context(viewport={'width': 375, 'height': 812})
        page = context.new_page()

        # Listen for console logs
        page.on("console", lambda msg: print(f"CONSOLE: {msg.text}"))
        page.on("pageerror", lambda exc: print(f"PAGE ERROR: {exc}"))

        # Load App
        print("Loading page...")
        page.goto("file:///app/index.html")

        # Wait for React to mount
        try:
            page.wait_for_selector("#root div", timeout=5000)
            print("React mounted successfully.")
        except:
            print("React failed to mount or took too long.")
            page.screenshot(path="verification/debug_fail.png")
            return

        # 1. Verify Home Screen & Mobile Nav
        print("Verifying Home...")
        time.sleep(1) # Wait for fade-in
        page.screenshot(path="verification/1_home.png")

        try:
            # 2. Go to Tracker
            print("Clicking Tracker...")
            page.locator(".glass-card", has_text="Daily Sales Tracker").click()

            # 3. Add Transaction
            print("Adding Transaction...")
            page.locator("select").first.select_option("samsung")
            page.get_by_placeholder("Model").fill("S24 Ultra")
            page.get_by_placeholder("Variant").fill("12/512")
            page.get_by_placeholder("Price").fill("120000")
            page.get_by_placeholder("Qty").fill("1")
            page.get_by_text("Add to Queue").click()

            page.screenshot(path="verification/2_queue.png")
            page.get_by_role("button", name="Save to").click()

            print("Verifying Transaction List...")
            page.wait_for_selector("text=S24 Ultra")
            page.screenshot(path="verification/3_transaction_saved.png")

            # 4. Settings Modal
            print("Verifying Settings...")
            # Click the logo instead, which also goes home and is robust
            page.locator("header").locator(".cursor-pointer").click()

            page.locator(".glass-card", has_text="Incentive Calculator").click()
            page.locator("button[title='Settings']").click()

            print("Waiting for Global Settings modal...")
            page.wait_for_selector("text=Global Settings")

            print("Switching to Wearables (wr)...")
            # Using text based selector for buttons in settings modal
            page.locator("button", has_text="wr").click()
            time.sleep(0.5)
            page.screenshot(path="verification/4_settings_wr.png")

            print("Switching to Accessories (acc)...")
            page.locator("button", has_text="acc").click()
            time.sleep(0.5)
            page.screenshot(path="verification/5_settings_acc.png")

            # 5. Charts
            print("Verifying Analytics...")
            page.get_by_text("Cancel").click()
            # Click logo to go home
            page.locator("header").locator(".cursor-pointer").click()

            page.locator(".glass-card", has_text="Daily Sales Tracker").click()
            page.get_by_text("ANALYTICS").click()
            time.sleep(1)
            page.screenshot(path="verification/6_analytics.png")
            print("Verification Complete!")

        except Exception as e:
            print(f"Script Error: {e}")
            import traceback
            traceback.print_exc()
            page.screenshot(path="verification/error_state.png")

        browser.close()

if __name__ == "__main__":
    run()
