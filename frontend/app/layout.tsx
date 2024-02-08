import type { Metadata } from "next";
import "./globals.css";
import { Open_Sans } from "next/font/google";

import { ThemeProvider } from "@/components/providers/ThemeProvider";
import { ModalProvider } from "@/components/providers/ModalProvider";
import { cn } from "@/lib/utils";

const font = Open_Sans({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Create Next App",
  description: "Generated by create next app",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko" suppressHydrationWarning>
      <body className={cn(font.className, "bg-white")}>
        <ThemeProvider
          attribute="class"
          defaultTheme="system"
          enableSystem
        >
          <ModalProvider />
          {children}
        </ThemeProvider>
      </body>
    </html>
  );
}
