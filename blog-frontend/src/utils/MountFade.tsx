// utils/useMountFade.tsx  (helper hook, optional)
import { useEffect, useState } from 'react';

export function useMountFade(delay = 10) {
    const [mounted, setMounted] = useState(false);
    useEffect(()=> {
        const t = setTimeout(()=> setMounted(true), delay);
        return () => clearTimeout(t);
    }, [delay])
    return mounted;
}