
import asyncio
from playwright.async_api import async_playwright
import os

async def run():
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        page = await browser.new_page()

        # Open the local index.html file
        file_path = os.path.abspath("index.html")
        await page.goto(f"file://{file_path}")

        # Wait for React to mount and show the welcome message
        try:
            # Check for key elements of the new UI
            await page.wait_for_selector("text=Welcome Back", timeout=10000)
            print("SUCCESS: 'Welcome Back' found.")

            # Check for Dark Mode toggle presence (moon/sun icon usually implies it, but we check button)
            await page.wait_for_selector("button >> i[data-lucide='moon']", timeout=5000)
            print("SUCCESS: Dark Mode toggle found.")

            # Navigate to Sales Tracker
            await page.click("text=Daily Sales Tracker")
            await page.wait_for_selector("text=Queue (0)", timeout=5000)
            print("SUCCESS: Navigated to Sales Tracker.")

            # Check for DATA tab
            await page.wait_for_selector("text=DATA", timeout=5000)
            print("SUCCESS: 'DATA' tab found.")

            # Navigate to Incentive Calculator
            await page.click("button >> i[data-lucide='home']") # Go Home first
            await page.wait_for_selector("text=Incentive Calculator")
            await page.click("text=Incentive Calculator")

            # Check for new Categories
            await page.wait_for_selector("text=Note PC", timeout=5000)
            print("SUCCESS: 'Note PC' category found.")

            print("Frontend verification PASSED.")

        except Exception as e:
            print(f"FAILED: {e}")
            await page.screenshot(path="frontend_error.png")

        await browser.close()

if __name__ == "__main__":
    asyncio.run(run())
