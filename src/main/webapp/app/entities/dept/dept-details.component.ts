import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import DeptService from './dept.service';
import { type IDept } from '@/shared/model/dept.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DeptDetails',
  setup() {
    const deptService = inject('deptService', () => new DeptService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const dept: Ref<IDept> = ref({});

    const retrieveDept = async deptId => {
      try {
        const res = await deptService().find(deptId);
        dept.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.deptId) {
      retrieveDept(route.params.deptId);
    }

    return {
      alertService,
      dept,

      previousState,
    };
  },
});
